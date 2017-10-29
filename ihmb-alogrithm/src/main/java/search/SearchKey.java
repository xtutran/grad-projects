package search;

import features.HSV166Histogram.DistanceFunction;

public class SearchKey {
	private String imagePath;
	private DistanceFunction distance;
	
	public SearchKey(String imagePath, DistanceFunction distance) {
		super();
		this.setImagePath(imagePath);
		this.setDistance(distance);
	}

	public String getImagePath() {
		return imagePath;
	}

	private void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public DistanceFunction getDistance() {
		return distance;
	}

	private void setDistance(DistanceFunction distance) {
		this.distance = distance;
	}
	
	@Override
    public boolean equals(Object p) {
        if (p instanceof SearchKey) {
            return (imagePath.equals(((SearchKey) p).getImagePath()) && 
            		distance.equals(((SearchKey) p).getDistance()));
        }
        return false;
    }
	
	@Override
	public String toString() {
		return String.format("%s@%s", this.getImagePath(), this.getDistance().name());
	}
	
	@Override
	public int hashCode() {
		return this.imagePath.hashCode() * this.distance.name().hashCode();
	}
}
