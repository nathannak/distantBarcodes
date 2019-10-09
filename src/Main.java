import java.util.*;

public class Main {

    public static void main(String[] args) {

        //int[] arr = new int[] {1,1,1,1,2,2,3,3};
        int[] arr = new int[] {51,83,51,40,51,40,51,40,83,40,83,83,51,40,40,51,51,51,40,40,40,83,51,51,40,51,51,40,40,51,51,40,51,51,51,40,83,40,40,83,51,51,51,40,40,40,51,51,83,83,40,51,51,40,40,40,51,40,83,40,83,40,83,40,51,51,40,51,51,51,51,40,51,83,51,83,51,51,40,51,40,51,40,51,40,40,51,51,51,40,51,83,51,51,51,40,51,51,40,40};

        int[] _arr = rearrangeBarcodes(arr);

        for(int elem : _arr){
            System.out.println(elem);
        }
    }

    public static int[] rearrangeBarcodes(int[] barcodes) {

        //Arrays with less than two items should be returned the way they are
        if(barcodes.length<=2) return barcodes;

        int[] map = new int[10001];

        //map frequency to element and keep them sorted by ascending order
        TreeMap<Integer, Queue<Integer>> hashMap = new TreeMap<>();

        //map barcodes values to their frequency
        for (int i = 0; i < barcodes.length; i++) {
            map[barcodes[i]]++;

        }

        //populate treeMap to get most frequent and associated items
        for (int i = 0; i < map.length; i++) {

            if (map[i] > 0) {

                if ( !hashMap.containsKey(map[i]) ) {
                    hashMap.put(map[i], new LinkedList<>());
                }

                //map frequency => item
                Queue<Integer> q = hashMap.get(map[i]);
                q.add(i);
                hashMap.put(map[i], q);
            }
        }

        //sort map to have the most freqency at last item
        Arrays.sort(map);

        //res is going to be returned in form of array at the end of function
        List<Integer> res = new ArrayList<>();

        while(map[map.length-1]!=0){

            int j = map.length-1;

            //get the highest frequency
            Queue<Integer> q = hashMap.get( map[j] );

            //get values of an item associated with highest frequency
            int count = q.poll(); //evict form group
            int max=count;

            //local cqche to save updates and apply to hashMap later on
            Map<Integer,Queue<Integer>> _map = new HashMap<>();

            //keep adding items to res list
            while(map[j]!=0) {

                //this is the case where we have an item left[the one with highest freq] and all other items have been consumed
                if( j==map.length-1 && map[j-1]==0 && map[j]>=1) {

                    int k=0;

                    while( k+1<res.size() && (res.get(k)==max || res.get(k+1)==max) ){
                        k++;
                    }

                    res.add(k+1,count);

                }else{

                    res.add(count);

                }

                //adjust count to be added in next while loop iteration
                map[j]--;

                //save and update hashMap later
                if(!_map.containsKey(map[j])){
                    _map.put(map[j],new LinkedList<>());
                }

                _map.get(map[j]).add(count);

                //move down the map to get then next eleemnt yuyou want to add
                j--;

                //now that j is decremented; extract new count to be added to res
                Queue<Integer> _q = hashMap.get( map[j] );

                //we may hit empty/null queue
                if(_q==null || _q.size()==0) break;

                //get new count and continue while loop
                count = _q.poll();

            }

            //sort array at each iteration
            Arrays.sort(map);

            //clean hashmap [remove old frequencies which are now decreased by 1]
            Iterator<Map.Entry<Integer, Queue<Integer>>> iter =  hashMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, Queue<Integer>> entry = iter.next();
                iter.remove();

//                 Queue<Integer> _q = entry.getValue();
//                 if (_q == null || _q.size() == 0) {
//                     iter.remove(); // always use remove() method of iterator
//                 }

            }

            //apply saved changes to hashMap
            for( Map.Entry<Integer,Queue<Integer>> entry : _map.entrySet() ){

                if(!hashMap.containsKey(entry.getKey())) hashMap.put( entry.getKey() , new LinkedList<>() );

                //changes to be applied here
                Queue<Integer> _q = hashMap.get(entry.getKey());

                //changes to be applied from
                Queue<Integer> toBeAddedQ = entry.getValue();

                if(_q!=null && toBeAddedQ!=null) {
                    while (!toBeAddedQ.isEmpty()) {
                        _q.offer(toBeAddedQ.poll());
                    }
                }
            }
        }

        return res.stream().mapToInt(i->i).toArray();

    }

}
