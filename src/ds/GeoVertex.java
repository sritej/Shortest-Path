package ds;

/**
 * A vertex, that also contains geo position (longitude + latitude)
 * 
 * @author michael
 * 
 */
public class GeoVertex extends Vertex {
	protected double latitude = 0;
	protected double longitude = 0;

	/**
	 * init constructor
	 * 
	 * @param id
	 * @param longitude
	 * @param latitude
	 */
	public GeoVertex(int id, double longitude, double latitude) {
		super(id);
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/**
	 * calculate the distance from this vertex to the parameter
	 * 
	 * @param GeoVertex
	 * @return distance
	 */
	public double getDistanceTo(GeoVertex gv) {
		return calculateLB(longitude, latitude, gv.longitude, gv.latitude);
	}

	/**
	 * 
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Provided code that calculates the distance on a sphere.
	 * 
	 * @param long1
	 * @param lat1
	 * @param long2
	 * @param lat2
	 * @return
	 */
	private double calculateLB(double long1, double lat1, double long2, double lat2) {
		final double const1 = 360.0;
		final double const2 = 1000000;
		// conversion to radiant and standard M.m format
		lat1 = (lat1 * 2.0 * Math.PI) / const1 / const2;
		long1 = (long1 * 2.0 * Math.PI) / const1 / const2;
		lat2 = (lat2 * 2.0 * Math.PI) / const1 / const2;
		long2 = (long2 * 2.0 * Math.PI) / const1 / const2;
		// earth parameters
		final double a = 6378137.0; // Earth Major Axis (WGS84)
		final double b = 6356752.3142; // Minor Axis
		double f = (a - b) / a; // "Flattening"
		double e = 2.0 * f - f * f; // "Eccentricity"
		// calculate some aux values for first coordinate
		double beta = a / Math.sqrt(1.0 - e * Math.sin(lat1) * Math.sin(lat1));
		double cos = Math.cos(lat1);
		// calculate some aux values for second coordinate w.r.t. first coordinate
		beta = a / Math.sqrt(1.0 - e * Math.sin(lat2) * Math.sin(lat2));
		cos = Math.cos(lat2);
		double x = beta * cos * Math.cos(long1);
		x -= beta * cos * Math.cos(long2);
		double y = beta * cos * Math.sin(long1);
		y -= beta * cos * Math.sin(long2);
		double z = beta * (1.0 - e) * Math.sin(lat1);
		z -= beta * (1 - e) * Math.sin(lat2);
		// calculate distance in meters
		double dist = Math.sqrt((x * x) + (y * y) + (z * z));
		// apply some factor in order to obtain admissible lower bounds
		final double magicFactorForAdmissableLowerBounds = 7.0;
		dist *= magicFactorForAdmissableLowerBounds;
		// we're done
		return dist;
	}
}
