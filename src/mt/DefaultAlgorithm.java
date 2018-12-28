package mt;

public enum DefaultAlgorithm {

    ADDITION("Сумма", getAdditionAlg()),
    PRODUCTION("Разность", getSubtractionAlg());

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
        MT mt = new MT(new Character[] {'*', ' ', '_'}, 3);

        mt.addInstruction(new Key('*', 1), new Instruction(' ', 3, Movement.RIGHT));
        mt.addInstruction(new Key(' ', 1), new Instruction(null, 1, Movement.RIGHT));

        mt.addInstruction(new Key('*', 2), new Instruction(null, 2, Movement.LEFT));
        mt.addInstruction(new Key(' ', 2), new Instruction(null, 1, Movement.RIGHT));
        mt.addInstruction(new Key('_', 2), new Instruction(null, 2, Movement.LEFT));

        mt.addInstruction(new Key('*', 3), new Instruction(null, 3, Movement.RIGHT));
        mt.addInstruction(new Key(' ', 3), new Instruction('*', 2, Movement.STAND));
        mt.addInstruction(new Key('_', 3), new Instruction(null, 3, Movement.RIGHT));
        return mt;
    }

    private static MT getProductionAlg() {
        MT mt = new MT(new Character[] {'1', '^', '*', 'a'}, 4);

        mt.addInstruction(new Key('1', 1), new Instruction('a', 3, Movement.RIGHT));
        mt.addInstruction(new Key('^', 1), new Instruction(null, 1, Movement.RIGHT));
        mt.addInstruction(new Key('*', 1), new Instruction(null, 4, Movement.STAND));
        mt.addInstruction(new Key('a', 1), new Instruction(null, 1, Movement.RIGHT));

        mt.addInstruction(new Key('1', 2), new Instruction(null, 2, Movement.LEFT));
        mt.addInstruction(new Key('^', 2), new Instruction(null, 1, Movement.RIGHT));
        mt.addInstruction(new Key('*', 2), new Instruction(null, 2, Movement.LEFT));
        mt.addInstruction(new Key('a', 2), new Instruction(null, 1, Movement.RIGHT));

        mt.addInstruction(new Key('1', 3), new Instruction(null, 3, Movement.RIGHT));
        mt.addInstruction(new Key('^', 3), new Instruction('1', 2, Movement.STAND));
        mt.addInstruction(new Key('*', 3), new Instruction(null, 3, Movement.RIGHT));
        mt.addInstruction(new Key('a', 3), new Instruction('1', 2, Movement.STAND));

        mt.addInstruction(new Key('^', 4), new Instruction(null, 1, Movement.RIGHT));
        mt.addInstruction(new Key('*', 4), new Instruction('^', 4, Movement.STAND));
        return mt;
    }

    private static MT getSubtractionAlg() {
        MT mt = new MT(new Character[] {'1', ' ', ' ', '0'}, 8);

        mt.addInstruction(new Key(' ', 1), new Instruction(' ', 2, Movement.RIGHT));
        mt.addInstruction(new Key('0', 1), new Instruction('0', 1, Movement.RIGHT));
        mt.addInstruction(new Key('1', 1), new Instruction('1', 1, Movement.RIGHT));

        mt.addInstruction(new Key(' ', 2), new Instruction(' ', 3, Movement.LEFT));
        mt.addInstruction(new Key('0', 2), new Instruction('0', 2, Movement.RIGHT));
        mt.addInstruction(new Key('1', 2), new Instruction('1', 2, Movement.RIGHT));

        mt.addInstruction(new Key(' ', 3), new Instruction(' ', 5, Movement.RIGHT));
        mt.addInstruction(new Key('0', 3), new Instruction('0', 3, Movement.LEFT));
        mt.addInstruction(new Key('1', 3), new Instruction('0', 4, Movement.LEFT));

        mt.addInstruction(new Key(' ', 4), new Instruction(' ', 8, Movement.LEFT));
        mt.addInstruction(new Key('0', 4), new Instruction('0', 4, Movement.LEFT));
        mt.addInstruction(new Key('1', 4), new Instruction('1', 4, Movement.LEFT));

        mt.addInstruction(new Key(' ', 5), new Instruction(' ', 6, Movement.LEFT));
        mt.addInstruction(new Key('0', 5), new Instruction('0', 5, Movement.RIGHT));
        mt.addInstruction(new Key('1', 5), new Instruction('1', 5, Movement.RIGHT));

        mt.addInstruction(new Key(' ', 6), new Instruction(' ', 7, Movement.LEFT));
        mt.addInstruction(new Key('0', 6), new Instruction(' ', 6, Movement.LEFT));
        mt.addInstruction(new Key('1', 6), new Instruction('1', 6, Movement.LEFT));

        //mt.addInstruction(new Key(' ', 7), new Instruction(' ', 3, Movement.LEFT));
        mt.addInstruction(new Key('0', 7), new Instruction(' ', 7, Movement.LEFT));
        mt.addInstruction(new Key('1', 7), new Instruction('1', 7, Movement.LEFT));

        //mt.addInstruction(new Key(' ', 8), new Instruction(' ', 3, Movement.LEFT));
        mt.addInstruction(new Key('0', 8), new Instruction('0', 8, Movement.LEFT));
        mt.addInstruction(new Key('1', 8), new Instruction('0', 1, Movement.RIGHT));

        return mt;
    }
}
