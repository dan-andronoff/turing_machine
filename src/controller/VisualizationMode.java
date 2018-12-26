package controller;

public enum VisualizationMode {

    STEP_BY_STEP("Пошаговый"),
    TIMER("По таймеру"),
    NO_VISUALIZATION("Выдать результат");

    private String name;

    VisualizationMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
