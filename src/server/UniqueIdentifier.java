package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by M4teo on 17.04.2017.
 */
public class UniqueIdentifier {
    private static List<Integer> ids = new ArrayList<Integer>();
    private static final int RANGE = 1000;//number of possible identifiers

    private static int index = 0;

    static
    {
        for (int i = 0; i < RANGE; i++)
            ids.add(i);

        Collections.shuffle(ids);
    }

    public static int getIdentifier()
    {
        if(index > ids.size() - 1 )
            index = 0;

        return ids.get(index++);
    }
}
