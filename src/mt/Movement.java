package mt;

public enum Movement {
    LEFT('Л'),
    RIGHT('П'),
    STAND('Н');

    private Character name;

    Movement(Character name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
