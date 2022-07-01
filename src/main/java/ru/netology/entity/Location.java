package ru.netology.entity;

public class Location {

    private final String city;

    private final Country country;

    private final String street;

    private final int builing;

    public Location(String city, Country country, String street, int builing) {
        this.city = city;
        this.country = country;
        this.street = street;
        this.builing = builing;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public String getStreet() {
        return street;
    }

    public int getBuiling() {
        return builing;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(Location.class)) {
            return false;
        }
        Location location = (Location) obj;

        return ((country == location.country || (country == null && location.country == null))
                && (city == location.city || (city == null && location.city == null))
                && (street == location.street || (street == null && location.street == null))
                && (builing == location.builing || (builing == 0 && location.builing == 0)));
    }

}
