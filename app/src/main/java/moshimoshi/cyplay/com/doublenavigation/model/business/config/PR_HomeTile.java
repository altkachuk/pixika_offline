package moshimoshi.cyplay.com.doublenavigation.model.business.config;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by damien on 25/07/16.
 */
@Parcel
public class PR_HomeTile {

    String title;
    String icon;
    PR_HomeTileBackground background;
    String action;
    String textColor;
    PR_HomeTilePosition position;
    PR_HomeTileSize size;

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }

    public String getTextColor() {
        return textColor;
    }

    public PR_HomeTileBackground getBackground() {
        return background;
    }

    public String getAction() {
        return action;
    }

    public PR_HomeTilePosition getPosition() {
        return position;
    }

    public PR_HomeTileSize getSize() {
        return size;
    }

    public static int getGridWidth(List<PR_HomeTile> homeTiles){
        int result = 0;
        for ( PR_HomeTile homeTile : homeTiles){
            result = Math.max(result, homeTile.getPosition().getX()+homeTile.getSize().getW());
        }
        return result;
    }

    public static int getGridHeight(List<PR_HomeTile> homeTiles){
        int result = 0;
        for ( PR_HomeTile homeTile : homeTiles){
            result = Math.max(result, homeTile.getPosition().getY()+homeTile.getSize().getH());
        }
        return result;
    }



}
