package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {
    List<CensusDTO> censusList=null;
    Map<String, CensusDTO> censusMap=null;
    public enum Country{
        INDIA,US;
    }
    public CensusAnalyser() {
        censusList=new ArrayList<>();
        censusMap=new HashMap<>();
    }

    public int loadCensusData(Country country,String ...csvFilePath){
        censusMap=new CensusLoader().loadCensusData(country,csvFilePath);
        censusList=censusMap.values().stream().collect(Collectors.toList());
        return censusMap.size();
    }

    private <E>int getCount(Iterator<E> censusCSVIterator) {
        Iterable<E> censusIterator=()-> censusCSVIterator;
        int namOfEateries = (int) StreamSupport.stream(censusIterator.spliterator(),false).count();
        return namOfEateries;
    }

    public String getStateWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.state);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;

    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for(int i=0;i<censusList.size()-1;i++){
            for(int j=0;j<censusList.size()-i-1;j++){
                CensusDTO census1=censusList.get(j);
                CensusDTO census2=censusList.get(j+1);
                if(censusCSVComparator.compare(census1,census2)>0){
                    censusList.set(j,census2);
                    censusList.set(j+1,census1);
                }
            }
        }
    }

    public String getPopulationWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA); }
        Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.population);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }

    public String getDensityWiseSortCensusData() {
        if(censusMap.size()==0 || censusMap==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA); }
        Comparator<CensusDTO> censusCSVComparator=Comparator.comparing(StateCensus->StateCensus.populationDensity);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }
}

