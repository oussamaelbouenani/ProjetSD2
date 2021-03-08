import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListeDAdjacence extends Graph{
	
	private Map<Country,Set<Route>> outputRoutes;

	public ListeDAdjacence(){
		super();
		outputRoutes=new HashMap<Country,Set<Route>>();

	}

	@Override
	// Complexit�: ?
	protected void ajouterSommet(Country a) {
		//� compl�ter
		outputRoutes.put(a, new HashSet<>());
	}

	@Override
	// Complexit�: ?
	protected void ajouterArc(Route f) {
		//� compl�ter
		outputRoutes.get(f.getSource()).add(f);
	}

	@Override
	// Complexit�: ?
	public Set<Route> arcsSortants(Country a) {
		//� compl�ter
		return outputRoutes.get(a);
	}

	@Override
	// Complexit�: ?
	public boolean sontAdjacents(Country a1, Country a2) {
		// � compl�ter
		Set<Route> set1 = outputRoutes.get(a1);
		Set<Route> set2 = outputRoutes.get(a2);

		for (Route f1:
			 set1) {
			if (a2.equals(f1.getDestination())) return true;
		}
		for (Route f2:
			 set2) {
			if (a1.equals(f2.getDestination())) return true;
		}
		return false;
	}

}
