import java.util.Arrays;

class RoadStatus {
    private int[] cars = new int[3];
    private int greenLightIndex = -1;
    private int greenLightDuration = 0;
    private int lastUpdateTime = -1;
    private boolean updatedThisUnit = false;

    public int[] roadStatus(int time) {
        updateTrafficLight(time);
        // System.out.print(time);
        // System.out.println(": " + Arrays.toString(cars));

        return Arrays.copyOf(cars, cars.length);
    }

    private void carExit(int currentTime) {
        return;
    }

    private void updateTrafficLight(int currentTime) {
        while (currentTime > lastUpdateTime) {
            if (greenLightIndex != -1 && greenLightDuration > 0) {
                int elapsed = currentTime - lastUpdateTime;
                int carsExited = Math.min(elapsed, greenLightDuration);
                cars[greenLightIndex] = Math.max(0, cars[greenLightIndex] - carsExited);
                lastUpdateTime += carsExited;
                greenLightDuration -= carsExited;
            } else {
                lastUpdateTime = currentTime;
            }
            updatedThisUnit = false; // Reset for potential updates in a new unit

            if (greenLightDuration <= 0 && !updatedThisUnit) {
                decideNextGreenLight();
            }

        }
    }

    private void decideNextGreenLight() {
        greenLightIndex = -1;
        int maxCars = 0;
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] > maxCars) {
                maxCars = cars[i];
                greenLightIndex = i;
            }
        }
        greenLightDuration = maxCars;
        if (maxCars == 0) {
            greenLightIndex = -1; // All lights to Red if no cars are present
        }
    }

    public void addCar(int time, int id, int num_of_cars) {
        if (time - 1 > lastUpdateTime) {
            updateTrafficLight(time - 1);
        }
        cars[id] += num_of_cars;
        if (time > lastUpdateTime) {
            updateTrafficLight(time);
        }
        if (!updatedThisUnit && greenLightDuration <= 0) {
            decideNextGreenLight();
            updatedThisUnit = true;
        }
    }

    RoadStatus() {
        Arrays.fill(cars, 0);
    }

    public static void main(String[] args) {
        RoadStatus sol1 = new RoadStatus(); // create a T-junction; all traffic lights are Red at the beginning
        // { "answer": [0, 0, 0], "func": "roadStatus", "args": [2] },
        // { "func": "addCar", "args": [6, 1, 5] },
        // { "answer": [0, 5, 0], "func": "roadStatus", "args": [6] },
        // { "func": "addCar", "args": [9, 0, 8] },
        // { "func": "addCar", "args": [10, 0, 5] },
        // { "func": "addCar", "args": [10, 1, 2] },
        // { "answer": [11, 2, 0], "func": "roadStatus", "args": [13] },
        System.out.println("2: " + Arrays.toString(sol1.roadStatus(2)));
        sol1.addCar(6, 1, 4);
        System.out.println("6: " + Arrays.toString(sol1.roadStatus(6)));
        sol1.addCar(9, 0, 3);
        System.out.println("9: " + Arrays.toString(sol1.roadStatus(9)));
        // System.out.println("10: " + Arrays.toString(sol1.roadStatus(10)));
        sol1.addCar(10, 1, 5);
        System.out.println("10: " + Arrays.toString(sol1.roadStatus(10)));
        // sol1.addCar(10, 1, 2);
        System.out.println("13: " + Arrays.toString(sol1.roadStatus(13)));
    }
}
