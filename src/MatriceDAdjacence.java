import java.util.*;

public class MatriceDAdjacence extends Graph{
	
	private Map<Integer, Country>  correspondanceIndiceCountry;
	private Map<Country, Integer>  correspondanceCountryIndice;
	private Route[][] matrice= new Route[0][0];
	private int nbCountry=0;

	public MatriceDAdjacence() {
		super();
		correspondanceCountryIndice= new HashMap<Country,Integer>();
		correspondanceIndiceCountry= new HashMap<Integer,Country>();
	}

	@Override
	protected void ajouterSommet(Country a) {
		//� compl�ter
		correspondanceIndiceCountry.put(nbCountry, a);
		correspondanceCountryIndice.put(a, nbCountry);
		nbCountry++;

		matrice = new Route[nbCountry][nbCountry];

	}

	@Override
	protected void ajouterArc(Route f) {
		//� compl�ter

		matrice[correspondanceCountryIndice.get(f.getSource())][correspondanceCountryIndice.get(f.getDestination())] = f;
	}

	@Override
	public Set<Route> arcsSortants(Country a) {
		//� compl�ter
		//return new HashSet<>(Arrays.asList(matrice[correspondanceCountryIndice.get(a)]).subList(0, nbCountry));

		Set<Route> set = new HashSet<>();

		for (int i = 0; i < nbCountry; i++) {
			Route f = matrice[correspondanceCountryIndice.get(a)][i];
			if (f != null)
				set.add(f);
		}
		return set;

	}

	@Override
	public boolean sontAdjacents(Country a1, Country a2) {
		// � compl�ter
		return matrice[correspondanceCountryIndice.get(a1)][correspondanceCountryIndice.get(a2)] != null;
	}
	
	

}
