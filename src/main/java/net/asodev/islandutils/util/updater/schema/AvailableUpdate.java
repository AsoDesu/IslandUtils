package net.asodev.islandutils.util.updater.schema;

public record AvailableUpdate(String title, String version, String releaseUrl) {
    public enum VersionState {
        PRE_RELEASE,
        UPDATE
    }
}
