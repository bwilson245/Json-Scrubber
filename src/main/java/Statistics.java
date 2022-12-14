public class Statistics {
    private double processTimeInMicroSeconds;
    private int totalElements;
    private int totalObjects;
    private int totalArrays;
    private int totalPrimitives;
    private int totalNull;
    private int totalScrubbedElements;

    public Statistics(double processTimeInMicroSeconds, int totalElements, int totalObjects, int totalArrays, int totalPrimitives,int totalNull, int totalScrubbedElements) {
        this.processTimeInMicroSeconds = processTimeInMicroSeconds;
        this.totalElements = totalElements;
        this.totalObjects = totalObjects;
        this.totalArrays = totalArrays;
        this.totalPrimitives = totalPrimitives;
        this.totalNull = totalNull;
        this.totalScrubbedElements = totalScrubbedElements;
    }

    public double getProcessTimeInMicroSeconds() {
        return processTimeInMicroSeconds;
    }
    public void setProcessTimeInMicroSeconds(double processTimeInMicroSeconds) {
        this.processTimeInMicroSeconds = processTimeInMicroSeconds;
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
    public int getTotalNull() {
        return totalNull;
    }
    public void setTotalNull(int totalNull) {
        this.totalNull = totalNull;
    }
    public int getTotalScrubbedElements() {
        return totalScrubbedElements;
    }
    public void setTotalScrubbedElements(int totalScrubbedElements) {
        this.totalScrubbedElements = totalScrubbedElements;
    }

    @Override
    public String toString() {
        return "Statistics{" + "processTimeInMicroSeconds=" + processTimeInMicroSeconds + ", totalElements=" + totalElements + ", totalObjects=" + totalObjects + ", totalArrays=" + totalArrays + ", totalPrimitives=" + totalPrimitives + ", totalNull=" + totalNull + ", totalScrubbedElements=" + totalScrubbedElements + '}';
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
        private int totalNull;
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
        public Builder withTotalNull(int totalNull) {
            this.totalNull = totalNull;
            return this;
        }
        public Builder withTotalScrubbedElements(int totalScrubbedElements) {
            this.totalScrubbedElements = totalScrubbedElements;
            return this;
        }
        public Statistics build() {
            return new Statistics(processTime, totalElements, totalObjects, totalArrays, totalPrimitives, totalNull, totalScrubbedElements);
        }
    }
}
