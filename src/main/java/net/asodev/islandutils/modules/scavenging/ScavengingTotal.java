package net.asodev.islandutils.modules.scavenging;

public record ScavengingTotal(String item, Long amount, ScavengingItemHandler handler) {
    public ScavengingTotal apply(ScavengingTotal total) {
        return new ScavengingTotal(this.item, this.amount + total.amount, this.handler);
    }

    public ScavengingTotal create(Long amount) {
        return new ScavengingTotal(this.item, amount, this.handler);
    }
}
