package natngs.nplanets.server.located.planets;

import natngs.nplanets.common.Location;
import natngs.nplanets.server.ILocated;

public class EllipsisOrbit2DPlanet extends Planet {
	private final ILocated center;
	private final ILocated excenter;
	private final ILocated notEllipticSimilar;

	public EllipsisOrbit2DPlanet(ILocated ref, ILocated excenter, ILocated notEllipticSimilar) {
		super(ref);
		this.center = ref;
		this.excenter = excenter;
		this.notEllipticSimilar = notEllipticSimilar;
	}

	@Override
	protected Location getRelativeLocation(double when) {
		Location a = this.center.getRelativeLocation(when);
		Location b = this.notEllipticSimilar.getRelativeLocation(when);
		
		double cx = a.get(0);
		double cy = a.get(1);
		double sx = b.get(0);
		double sy = b.get(1);
		
		double scy = sy-cy;
		if(scy == 0) {
			return new Location((cx+sx)/2.0, (cy+sy)/2.0);
		}

		Location e = this.excenter.getRelativeLocation(when);
		double ex = e.get(0);
		double ey = e.get(1);
		
		double bex = sx-ex;
		if(bex == 0) {
			return new Location(ex, (ex*(cx-sx) + ((cy+sy)*scy-(cx*cx-sx*sx))/2.0)/scy);
		}
		
		double ox = ((cy+sy-(cx*cx-sx*sx)/scy)/2.0*bex+ ex*sy + sx*ey)/(sy-ey-(cx-sx)*bex);
		return new Location(ox, (ox*(sy-ey) - ex*sy + sx*ey  )/bex);
	}
}