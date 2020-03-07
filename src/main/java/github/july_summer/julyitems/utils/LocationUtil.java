package github.july_summer.julyitems.utils;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    public static String replace(Location location, String text, String strX, String strY, String strZ){
        return text.replace(strX, String.valueOf(location.getX()))
                .replace(strY, String.valueOf(location.getY()))
                .replace(strZ, String.valueOf(location.getZ()));
    }

    public static String locToStr(Location location){
        return location.getX() + "," + location.getY() + "," + location.getZ();
    }

    public static Location strToLoc(World world, String str){
        String location[] = str.split(",");
        return new Location(world, Double.parseDouble(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]));
    }


}
