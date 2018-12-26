package mt;

public enum DefaultAlgorithm {

    ADDITION("Сумма", getAdditionAlg());

    private String name;
    private MT mt;

    DefaultAlgorithm(String name, MT mt) {
        this.name = name;
        this.mt = mt;
    }

    public MT getMt() {
        return mt;
    }

    @Override
    public String toString() {
        return name;
    }

    private static MT getAdditionAlg() {
        MT mt = new MT(new Character[] {'*', ',', '_'}, 3);

        mt.addInstruction(new Key('*', 1), new Instruction(',', 3, Movement.RIGHT));
        mt.addInstruction(new Key(',', 1), new Instruction(null, 1, Movement.RIGHT));

        mt.addInstruction(new Key('*', 2), new Instruction(null, 2, Movement.LEFT));
        mt.addInstruction(new Key(',', 2), new Instruction(null, 1, Movement.RIGHT));
        mt.addInstruction(new Key('_', 2), new Instruction(null, 2, Movement.LEFT));

        mt.addInstruction(new Key('*', 3), new Instruction(null, 3, Movement.RIGHT));
        mt.addInstruction(new Key(',', 3), new Instruction('*', 2, Movement.STAND));
        mt.addInstruction(new Key('_', 3), new Instruction(null, 3, Movement.RIGHT));
        return mt;
    }
}
