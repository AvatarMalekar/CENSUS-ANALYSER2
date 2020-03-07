package censusanalyser;

import com.google.gson.Gson;
import sun.text.resources.cldr.ext.FormatData_yo_BJ;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {
    private  Country country;
    List<CensusDTO> censusList=null;
    Map<String, CensusDTO> censusMap=null;
    HashMap<CountrySort,Comparator<CensusDTO>> mapOfSortValues=null;

    public enum Country{
        INDIA,US;
    }
    public enum CountrySort{
        STATE,POPULATION,AREA,DENSITY;
    }
    public CensusAnalyser() {
        censusList=new ArrayList<>();
        censusMap=new HashMap<>();
        mapOfSortValues=new HashMap<>();
        this.mapOfSortValues.put(CountrySort.STATE,Comparator.comparing(stateCensus->stateCensus.state));
        this.mapOfSortValues.put(CountrySort.POPULATION,Comparator.comparing(stateCensus->stateCensus.population));
        this.mapOfSortValues.put(CountrySort.AREA,Comparator.comparing(stateCensus->stateCensus.totalArea));
        this.mapOfSortValues.put(CountrySort.DENSITY,Comparator.comparing(stateCensus->stateCensus.populationDensity));
  }
    public CensusAnalyser(Country country) {
        this.country=country;
        censusList=new ArrayList<>();
        censusMap=new HashMap<>();
        mapOfSortValues=new HashMap<>();
        this.mapOfSortValues.put(CountrySort.STATE,Comparator.comparing(stateCensus->stateCensus.state));
        this.mapOfSortValues.put(CountrySort.POPULATION,Comparator.comparing(stateCensus->stateCensus.population));
        this.mapOfSortValues.put(CountrySort.AREA,Comparator.comparing(stateCensus->stateCensus.totalArea));
        this.mapOfSortValues.put(CountrySort.DENSITY,Comparator.comparing(stateCensus->stateCensus.populationDensity));
    }

    public int loadCensusData(Country country,String ...csvFilePath){
        censusMap= CensusAdapterFactory.getCensusAdapter(country,csvFilePath);
        censusList=censusMap.values().stream().collect(Collectors.toList());
        return censusMap.size();
    }

    private <E>int getCount(Iterator<E> censusCSVIterator) {
        Iterable<E> censusIterator=()-> censusCSVIterator;
        int namOfEateries = (int) StreamSupport.stream(censusIterator.spliterator(),false).count();
        return namOfEateries;
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

    public String getSortedCensusData(CountrySort density) {
        if(censusMap.size()==0 || censusList==null){
            throw new CensusAnalyserException(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA); }
        Comparator<CensusDTO> censusCSVComparator=this.mapOfSortValues.get(density);
        this.sort(censusCSVComparator);
        String json=new Gson().toJson(censusList);
        return json;
    }
}

