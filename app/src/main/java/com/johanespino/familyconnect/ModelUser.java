package com.johanespino.familyconnect;

public class ModelUser {
    String email, firstName, imagen, lastName, role, uid, onlineStatus, lat,lng;

    public ModelUser(String email, String firstName, String imagen, String lastName, String role, String uid, String onlineStatus, String lat, String lng) {
        this.email = email;
        this.firstName = firstName;
        this.imagen = imagen;
        this.lastName = lastName;
        this.role = role;
        this.uid = uid;
        this.onlineStatus = onlineStatus;
        this.lat = lat;
        this.lng = lng;
    }

    public ModelUser() {
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
