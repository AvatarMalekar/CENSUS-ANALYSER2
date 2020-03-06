package censusanalyser;

import java.util.HashMap;
import java.util.Map;

public class USCensusAdapter extends CensusAdapter{

    @Override
    Map<String, CensusDTO> loadCensusData(String... csvFilePath) {
        Map<String,CensusDTO> censusMap=new HashMap<>();
        censusMap=super.loadCensusData(USCensusCSV.class,csvFilePath[0]);
        return censusMap;
    }
}
