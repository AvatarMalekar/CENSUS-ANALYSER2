package censusanalyser;

public class CSVBuilderFactory {

    public static ICSVBuilder createBuilder() {
        return new OpenCSVBuilder();
    }
}
