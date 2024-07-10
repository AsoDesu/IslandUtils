package net.asodev.islandutils.modules.splits.sob;

import net.asodev.islandutils.modules.splits.LevelSplits;

import java.time.Duration;
import java.util.*;
import java.util.stream.IntStream;

public class SobCalc {
    private static final List<String> mainStages = List.of(
            "M1-1", "M1-2", "M1-3",
            "M2-1", "M2-2", "M2-3",
            "M3-1", "M3-2", "M3-3"
    );
    private static final List<String> bonusStages = List.of(
            "B1-1", "B1-2", "B1-3",
            "B2-1", "B2-2", "B2-3",
            "B3-1", "B3-2", "B3-3"
    );
    private static final List<String> finalStages = List.of(
            "greenB4-1", "yellowB4-1", "redB4-1"
    );
    private static final List<String> expertFinale = List.of(
            "redB4-1", "redT3-4"
    );
    private static final List<String> mainTransitions = List.of(
            "T0-1", "T1-2", "T2-3"
    );
    private static final List<String> bonusTransitions = List.of(
            "TB0-1", "TB1-2", "TB2-3"
    );

    public static Optional<Long> timesFromSplitsNoColor(LevelSplits splitInfo, List<String> splits) {
        List<Optional<Long>> splitList = splits
                .stream()
                .map(splitInfo::getSplitNoColor)
                .map(os -> os.map(LevelSplits.Split::best))
                .toList();
        if (splitList.stream().anyMatch(Optional::isEmpty)) return Optional.empty();
        return splitList
                .stream()
                .flatMap(Optional::stream)
                .reduce(Long::sum);
    }


    public static Optional<Long> timesFromSplitsRaw(LevelSplits splitInfo, List<String> splits) {
        List<Optional<Long>> splitList = splits
                .stream()
                .map(splitInfo::getRawSplit)
                .map(os -> os.map(LevelSplits.Split::best))
                .toList();
        if (splitList.stream().anyMatch(Optional::isEmpty)) return Optional.empty();
        return splitList
                .stream()
                .flatMap(Optional::stream)
                .reduce(Long::sum);
    }

    public static Optional<Long> bestFinal(LevelSplits splits) {
        long minTime = Long.MAX_VALUE;
        String minName = null;
        for (String splitName : finalStages) {
            Optional<LevelSplits.Split> splitTime = splits.getRawSplit(splitName);
            if (splitTime.isEmpty()) continue;
            long bestTime = splitTime.get().best();
            if (bestTime < minTime) {
                minTime = bestTime;
                minName = splitName;
            }
        }
        if (minName == null) return Optional.empty();
        String minColor = minName.split("B")[0];
        String transitionSplitName = String.format("%sT3-4", minColor);
        Optional<LevelSplits.Split> finaleTransitionTime = splits.getRawSplit(transitionSplitName);
        if (finaleTransitionTime.isEmpty()) return Optional.empty();
        return Optional.of(minTime
                + finaleTransitionTime.get().best()
                - 1500 // Compensate for 1500 ms delay
        );
    }

    public static Optional<Long> standardTime(LevelSplits splits) {
        Optional<Long> mainTime = timesFromSplitsNoColor(splits, mainStages);
        Optional<Long> mainTransitionsTime = timesFromSplitsRaw(splits, mainTransitions)
                .map(s -> s - 4500); // Compensate for three transition delays
        Optional<Long> finaleTime = bestFinal(splits);
        return mainTime
                .flatMap(m -> mainTransitionsTime
                        .flatMap(mt -> finaleTime
                                .map(ft -> m + mt + ft)));
    }

    public static Optional<List<Long>> getTimesFromStage(int howManyComplete, String stageName, LevelSplits splits) {
        List<Optional<Long>> times = IntStream
                .range(0, howManyComplete)
                .mapToObj(i -> String.format("%s-%d", stageName, i + 1))
                .map(splits::getSplitNoColor)
                .map(s -> s.map(LevelSplits.Split::best))
                .toList();
        if (times.stream().anyMatch(Optional::isEmpty)) return Optional.empty();
        return Optional.of(times.stream().flatMap(Optional::stream).toList());
    }

    public static Optional<Long> additionalBonusTime(int howManyComplete, String stageName, LevelSplits splits, long returnTime) {
        int stage = Integer.parseInt(String.valueOf(stageName.charAt(1)));
        if (howManyComplete == 0) {
            String transitionString = String.format("T%d-%d", stage - 1, stage);
            return splits
                    .getRawSplit(transitionString)
                    .map(LevelSplits.Split::best)
                    .map(s -> s - 1500); // Compensate for 1500 ms delay
        }
        long toAdd = returnTime;

        String splitName = String.format("TB%d-%d", stage - 1, stage);
        Optional<Long> tb = splits.getRawSplit(splitName).map(LevelSplits.Split::best);
        if (tb.isEmpty()) return Optional.empty();
        toAdd += tb.get();

        if (howManyComplete != 3) {
            Optional<Long> uncompletedStageSplit = splits.getRawSplit("U").map(LevelSplits.Split::best);
            if (uncompletedStageSplit.isEmpty()) return Optional.empty();
            toAdd += uncompletedStageSplit.get();
        }
        toAdd -= 1500; // Compensate for 1500 ms delay
        return Optional.of(toAdd);
    }

    public static String buildBonusPathString(int howManyComplete, String stage) {
        if (howManyComplete == 0) return "";
        StringBuilder str = new StringBuilder(" ->");
        for (int i = 1; i <= howManyComplete; i++) {
            str.append(String.format(" %s-%d -> ", stage, i));
        }
        str.append("R");
        return str.toString();
    }

    public static Optional<AdvancedInfo> advancedTime(LevelSplits splits) {
        Optional<Long> mainTime = timesFromSplitsNoColor(splits, mainStages);
        if (mainTime.isEmpty()) return Optional.empty();
        Optional<Long> returnTime = splits.getRawSplit("R").map(LevelSplits.Split::best);
        if (returnTime.isEmpty()) return Optional.empty();
        Map<Long, String> variants = new HashMap<>();
        for (int b1 = 0; b1 < 4; b1++) {
            for (int b2 = 0; b2 < 4; b2++) {
                for (int b3 = 0; b3 < 4; b3++) {
                    for (String finalS : finalStages) {
                        long medalsForFinish;
                        if (finalS.contains("green")) {
                            medalsForFinish = 1;
                        } else if (finalS.contains("yellow")) {
                            medalsForFinish = 2;
                        } else {
                            medalsForFinish = 3;
                        }
                        if (9 + b1 + b2 + b3 + medalsForFinish <= 16) continue;
                        Optional<LevelSplits.Split> finalStageSplit = splits.getRawSplit(finalS);
                        if (finalStageSplit.isEmpty()) continue;
                        long finaleTime = finalStageSplit.get().best();

                        Optional<List<Long>> b1_times = getTimesFromStage(b1, "B1", splits);
                        Optional<List<Long>> b2_times = getTimesFromStage(b2, "B2", splits);
                        Optional<List<Long>> b3_times = getTimesFromStage(b3, "B3", splits);
                        if (b1_times.isEmpty() || b2_times.isEmpty() || b3_times.isEmpty()) continue;
                        ArrayList<Long> combined = new ArrayList<>();

                        Optional<Long> b1_add = additionalBonusTime(b1, "B1", splits, returnTime.get());
                        Optional<Long> b2_add = additionalBonusTime(b2, "B2", splits, returnTime.get());
                        Optional<Long> b3_add = additionalBonusTime(b3, "B3", splits, returnTime.get());
                        if (b1_add.isEmpty() || b2_add.isEmpty() || b3_add.isEmpty()) continue;

                        combined.add(mainTime.get());
                        combined.addAll(b1_times.get());
                        combined.addAll(b2_times.get());
                        combined.addAll(b3_times.get());
                        combined.add(b1_add.get());
                        combined.add(b2_add.get());
                        combined.add(b3_add.get());
                        combined.add(finaleTime);

                        String path = String.format(
                                "START%s -> M1%s -> M2%s -> M3 -> %s",
                                buildBonusPathString(b1, "B1"),
                                buildBonusPathString(b2, "B2"),
                                buildBonusPathString(b3, "B3"),
                                finalS
                        );
                        long sum = combined.stream().reduce(Long::sum).get();
                        variants.put(sum, path);
                    }
                }
            }
        }
        return variants.keySet().stream().min(Long::compareTo).map(t -> new AdvancedInfo(t, variants.get(t)));
    }

    public static Optional<Long> expertTime(LevelSplits splits) {
        Optional<Long> mainTime = timesFromSplitsNoColor(splits, mainStages);
        Optional<Long> bonusTime = timesFromSplitsNoColor(splits, bonusStages);
        Optional<Long> bonusTransitionsTime = timesFromSplitsRaw(splits, bonusTransitions)
                .map(s -> s - 4500); // Compensate for three transition delays
        Optional<Long> returnsTime = splits.getRawSplit("R").map(LevelSplits.Split::best);
        Optional<Long> finaleTime = timesFromSplitsRaw(splits, expertFinale);
        if (mainTime.isEmpty() ||
                bonusTime.isEmpty() ||
                bonusTransitionsTime.isEmpty() ||
                finaleTime.isEmpty() ||
                returnsTime.isEmpty())
            return Optional.empty();

        return Optional.of(mainTime.get() +
                bonusTime.get() +
                bonusTransitionsTime.get() +
                (returnsTime.get() * 3 - 4500) // Compensate for three transition delays
                + finaleTime.get());
    }

    public static String format(long sum) {
        Duration duration = Duration.ofMillis(sum);
        long MM = duration.toMinutes();
        long SS = duration.toSecondsPart();
        long MS = duration.toMillisPart();
        return String.format(" (%02d:%02d.%03d)", MM, SS, MS);
    }
}
