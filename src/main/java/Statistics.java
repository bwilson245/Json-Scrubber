public class Statistics {
    private double processTime;
    private int totalElements;
    private int totalObjects;
    private int totalArrays;
    private int totalPrimitives;
    private int totalScrubbedElements;

    public Statistics(double processTime, int totalElements, int totalObjects, int totalArrays, int totalPrimitives, int totalScrubbedElements) {
        this.processTime = processTime;
        this.totalElements = totalElements;
        this.totalObjects = totalObjects;
        this.totalArrays = totalArrays;
        this.totalPrimitives = totalPrimitives;
        this.totalScrubbedElements = totalScrubbedElements;
    }

    public double getProcessTime() {
        return processTime;
    }
    public void setProcessTime(double processTime) {
        this.processTime = processTime;
    }
    public int getTotalElements() {
        return totalElements;
    }
    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
    public int getTotalObjects() {
        return totalObjects;
    }
    public void setTotalObjects(int totalObjects) {
        this.totalObjects = totalObjects;
    }
    public int getTotalArrays() {
        return totalArrays;
    }
    public void setTotalArrays(int totalArrays) {
        this.totalArrays = totalArrays;
    }
    public int getTotalPrimitives() {
        return totalPrimitives;
    }
    public void setTotalPrimitives(int totalPrimitives) {
        this.totalPrimitives = totalPrimitives;
    }
    public int getTotalScrubbedElements() {
        return totalScrubbedElements;
    }
    public void setTotalScrubbedElements(int totalScrubbedElements) {
        this.totalScrubbedElements = totalScrubbedElements;
    }

    @Override
    public String toString() {
        return "Statistics{" + "processTime=" + processTime + ", totalElements=" + totalElements + ", totalObjects=" + totalObjects + ", totalArrays=" + totalArrays + ", totalPrimitives=" + totalPrimitives + ", totalScrubbedElements=" + totalScrubbedElements + '}';
    }
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private double processTime;
        private int totalElements;
        private int totalObjects;
        private int totalArrays;
        private int totalPrimitives;
        private int totalScrubbedElements;

        private Builder() {
        }
        public Builder withProcessTime(double processTime) {
            this.processTime = processTime;
            return this;
        }
        public Builder withTotalElements(int totalElements) {
            this.totalElements = totalElements;
            return this;
        }
        public Builder withTotalObjects(int totalObjects) {
            this.totalObjects = totalObjects;
            return this;
        }
        public Builder withTotalArrays(int totalArrays) {
            this.totalArrays = totalArrays;
            return this;
        }
        public Builder withTotalPrimitives(int totalPrimitives) {
            this.totalPrimitives = totalPrimitives;
            return this;
        }
        public Builder withTotalScrubbedElements(int totalScrubbedElements) {
            this.totalScrubbedElements = totalScrubbedElements;
            return this;
        }
        public Statistics build() {
            return new Statistics(processTime, totalElements, totalObjects, totalArrays, totalPrimitives, totalScrubbedElements);
        }
    }
}
