package CHuffmanCompressor;

class CPriorityQueue {
    final int DEFAULTMAX = 256;

    private HuffmanNode[] nodes;
    private int capacity;
    private int total;

    public CPriorityQueue() {
        capacity = DEFAULTMAX;
        total = 0;
        nodes = new HuffmanNode[capacity];
    }

    public CPriorityQueue(int max) {
        capacity = max;
        total = 0;
        nodes = new HuffmanNode[capacity];
    }

    public boolean enqueue(HuffmanNode np) {
        if (isFull())
            return false;

        if (isEmpty()) { 
            nodes[total++] = np;
            return true;
        }

        int i = total - 1;
        while (i >= 0 && nodes[i].freq >= np.freq) {
            nodes[i + 1] = nodes[i];
            i--;
        }
        nodes[i + 1] = np;
        total++;
        return true;
    }

    public HuffmanNode dequeue() {
        if (isEmpty())
            return null;
        HuffmanNode ret = nodes[0];
        total--;
        for (int i = 0; i < total; i++)
            nodes[i] = nodes[i + 1];
        return ret;
    }

    public boolean isEmpty() {
        return (total == 0);
    }

    public boolean isFull() {
        return (total == capacity);
    }

    public int totalNodes() {
        return total;
    }

    
    public void displayQ() {
        for (int i = 0; i < total; i++) {
            System.out.println("Q" + i + ") " + nodes[i].ch + " : " + nodes[i].freq);
        }
    }
}
