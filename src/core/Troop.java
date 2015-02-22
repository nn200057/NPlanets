package core;

import java.util.HashMap;

import core.map.Planet;

import enums.CoefType;
import enums.Constants;

/**
 * 
 * @author Nathaël Noguès
 * 
 */
public class Troop {
	private int nbSoldiers;
	private Planet source;

	// When not in this map, value considered is "0" (usage: 2^coef)
	private HashMap<CoefType, Double> coefs;

	public Troop(Planet source, int nbSoldiers) {
		this.source = source;
		this.nbSoldiers = nbSoldiers;
		coefs = source.getAllCoefs();
	}

	// GETTERS and methods that don't modify planet objects //
	public double getCoef(CoefType type) {
		return coefs.get(type);
	}
	public int getNbSoldiers() {
		return nbSoldiers;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return nbSoldiers + " troupes";
	}

	// SETTERS and methods that modify planet objects //
	public boolean merge(Troop t2) {
		if (source != t2.source)
			return false;

		for(CoefType cType : CoefType.values()) {
			Double dc = coefs.get(cType);
			double val1 = dc == null?0:dc;

			dc = t2.coefs.get(cType);
			double val2 = dc == null?0:dc;

			if (val1 != val2)
				return false;
		}

		nbSoldiers += t2.nbSoldiers;
		t2.nbSoldiers = 0;
		return true;
	}

	public Troop split(int newTroupeNbPeople) {
		if (newTroupeNbPeople <= 0 || newTroupeNbPeople >= nbSoldiers)
			throw new IllegalArgumentException(this.getClass()
					+ "::split: impossible to split a troop with " + nbSoldiers
					+ " soldiers in 2 new troupes with " + newTroupeNbPeople
					+ " and " + (nbSoldiers - newTroupeNbPeople) + " soldiers.");
		nbSoldiers -= newTroupeNbPeople;
		Troop nt = new Troop(source, newTroupeNbPeople);
		return nt;
	}

	public void adaptCaracts(Planet p) {
		for (CoefType cType : CoefType.values()) {
			double tCoef = getCoef(cType);
			double pCoef = p.getCoef(cType);
			if (tCoef == pCoef)
				continue;

			double ecart = pCoef
					/ Constants.TroupeDefaultAdaptationTime.getValue();
			if (tCoef > pCoef) {
				double newValue = tCoef - ecart;
				if (newValue < pCoef)
					newValue = pCoef;
				coefs.put(cType, newValue);
			} else { // if (tCoef < pCoef)
				double newValue = tCoef + ecart;
				if (newValue > pCoef)
					newValue = pCoef;
				coefs.put(cType, newValue);
			}
		}
	}
}
