package sawachats.apps.alirabie.com.sawachats.Models;

/**
 * Created by Eng Ali on 10/23/2018.
 */

public class UserModel {
    private String Active;
    private String image;
    private String lastSeen;
    private String name;
    private String status;
    private String thump_image;

    public UserModel() {
    }

    public UserModel(String active, String image, String lastSeen, String name, String status, String thump_image) {
        Active = active;
        this.image = image;
        this.lastSeen = lastSeen;
        this.name = name;
        this.status = status;
        this.thump_image = thump_image;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThump_image() {
        return thump_image;
    }

    public void setThump_image(String thump_image) {
        this.thump_image = thump_image;
    }
}
