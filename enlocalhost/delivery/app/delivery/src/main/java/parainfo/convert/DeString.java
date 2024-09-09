package parainfo.convert;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DeString {

    public DeString() {
    }

    public static Integer aInteger(String s) {
        Integer result = null;

        if (s != null) {
            try {
                result = Integer.valueOf(s);

            } catch (NumberFormatException e) {
            }
        }

        return result;
    }

    public static Double aDouble(String s) {
        Double result = null;

        if (s != null) {
            try {
                result = Double.valueOf(s);

            } catch (NumberFormatException e) {
            }
        }

        return result;
    }

    /**
     * @param _ids cadena == "3,4,7,8,9"
     * @return lista de enteros
     */
    public static List<Integer> ids(String _ids) {
        List<Integer> list = null;

        if ((_ids != null) && (_ids.trim().length() > 0)) {
            String[] id = _ids.split(",");

            list = new ArrayList<>();
            
            for (String ix : id) {
                Integer x = aInteger(ix.trim());

                if (x != null) {
                    list.add(x);
                } else {
                    list = null;
                    break;
                }
            }
        }

        return list;
    }

    /**
     * @param fechahora como cadena
     * @return java.sql.Timestamp
     */
    public static Timestamp aTimestamp(String fechahora) {
        Timestamp result = null;

        SimpleDateFormat sdf
                = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setLenient(false);

        try {
            java.util.Date ufechahora = sdf.parse(fechahora);
            result = new java.sql.Timestamp(ufechahora.getTime());

        } catch (ParseException ex) {
        }

        return result;
    }
}
