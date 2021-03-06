package org.solovyev.android.calculator.plot;

import org.jetbrains.annotations.NotNull;

class GraphData {

    private int size = 0;

    private int allocatedSize = 4;
    private float[] xs = new float[allocatedSize];
    private float[] ys = new float[allocatedSize];

    private GraphData() {
    }

    @NotNull
    static GraphData newEmptyInstance() {
        return new GraphData();
    }

    void swap(@NotNull GraphData that) {
        float savedXs[] = that.xs;
        float savedYs[] = that.ys;
        int savedSize = that.size;
        int savedAllocatedSize = that.allocatedSize;

        that.xs = this.xs;
        that.ys = this.ys;
        that.size = this.size;
        that.allocatedSize = this.allocatedSize;

        this.xs = savedXs;
        this.ys = savedYs;
        this.size = savedSize;
        this.allocatedSize = savedAllocatedSize;
    }

    void push(float x, float y) {
        if (size >= allocatedSize) {
            makeSpaceAtTheEnd(size + 1);
        }

        xs[size] = x;
        ys[size] = y;
        ++size;
    }

    private void makeSpaceAtTheEnd(int newSize) {
        int oldAllocatedSize = allocatedSize;
        while (newSize > allocatedSize) {
            allocatedSize += allocatedSize;
        }

        if (oldAllocatedSize != allocatedSize) {
            float[] a = new float[allocatedSize];
            System.arraycopy(xs, 0, a, 0, this.size);
            xs = a;
            a = new float[allocatedSize];
            System.arraycopy(ys, 0, a, 0, this.size);
            ys = a;
        }
    }


    float getLastX() {
        return xs[size - 1];
    }

    float getLastY() {
        return ys[size - 1];
    }

    float getFirstX() {
        return xs[0];
    }

    float getFirstY() {
        return ys[0];
    }

    void pop() {
        --size;
    }

    boolean empty() {
        return size == 0;
    }

    void clear() {
        size = 0;
    }

    void eraseBefore(float x) {
        int i = 0;
        while (i < size && xs[i] < x) {
            ++i;
        }
        // step back as xs[i] >= x and xs[i-1] < x
        --i;

        if (i > 0) {
            size -= i;
            System.arraycopy(xs, i, xs, 0, size);
            System.arraycopy(ys, i, ys, 0, size);
        }
    }

    void eraseAfter(float x) {
        int i = size - 1;
        while (i >= 0 && x < xs[i]) {
            --i;
        }

        // step next as xs[i] > x and xs[i+1] <= x
        ++i;

        if (i < size - 1) {
            size = i + 1;
        }
    }

    int findPositionAfter(float x, float y) {
        int i = 0;
        while (i < size && xs[i] <= x) {
            ++i;
        }

        if (Float.isNaN(y)) {
            while (i < size && Float.isNaN(ys[i])) {
                ++i;
            }
        }

        return i;
    }

    void append(GraphData that) {
        makeSpaceAtTheEnd(size + that.size);
        int position = that.findPositionAfter(xs[size - 1], ys[size - 1]);
        System.arraycopy(that.xs, position, xs, size, that.size - position);
        System.arraycopy(that.ys, position, ys, size, that.size - position);
        size += that.size - position;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(size).append(": ");
        for (int i = 0; i < size; ++i) {
            b.append(xs[i]).append(", ");
        }
        return b.toString();
    }

    public float[] getXs() {
        return xs;
    }

    public float[] getYs() {
        return ys;
    }

    public int getSize() {
        return size;
    }
}
