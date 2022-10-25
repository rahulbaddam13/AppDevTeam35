package edu.northeastern.numad22fa_mrp;

public class WebPhoto {

    private String id;
    private String farm_id;
    private String server_id;
    private String secret;

    public WebPhoto(String id, String farm_id, String server_id, String secret) {
        this.id = id;
        this.farm_id = farm_id;
        this.server_id = server_id;
        this.secret = secret;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public void setFarm_id(String farm_id) {
        this.farm_id = farm_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
