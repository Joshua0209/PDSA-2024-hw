import java.util.*;

class Exam {
    public static List<int[]> getPassedList(Integer[][] scores) {
        int numberOfSubjects = scores.length;
        int numberOfStudents = scores[0].length;
        List<Set<Integer>> top20PercentStudentsPerSubject = new ArrayList<>();

        // Determine the top 20% thresholds and track students per subject
        for (Integer[] subjectScores : scores) {
            Integer[] sortedScores = Arrays.copyOf(subjectScores, subjectScores.length);
            Arrays.sort(sortedScores, Collections.reverseOrder());
            int thresholdIndex = (int) Math.ceil(numberOfStudents * 0.2) - 1;
            int thresholdScore = sortedScores[thresholdIndex];
            Set<Integer> topStudents = new HashSet<>();
            for (int i = 0; i < numberOfStudents; i++) {
                if (subjectScores[i] >= thresholdScore) {
                    topStudents.add(i);
                }
            }
            top20PercentStudentsPerSubject.add(topStudents);
        }

        // Find the intersection of all sets to get students in the top 20% across all
        // subjects
        Set<Integer> qualifiedStudents = new HashSet<>(top20PercentStudentsPerSubject.get(0));
        for (Set<Integer> subjectTopStudents : top20PercentStudentsPerSubject) {
            qualifiedStudents.retainAll(subjectTopStudents);
        }

        // Calculate total scores for qualified students
        List<int[]> resultList = new ArrayList<>();
        for (Integer studentId : qualifiedStudents) {
            int totalScore = 0;
            for (Integer[] subjectScores : scores) {
                totalScore += subjectScores[studentId];
            }
            resultList.add(new int[] { studentId, totalScore });
        }

        // Sort by total score in descending order
        resultList.sort((a, b) -> b[1] != a[1] ? Integer.compare(b[1], a[1]) : Integer.compare(a[0], b[0]));

        return resultList;
    }

    public static void main(String[] args) {
        // Test the method with sample data
        List<int[]> ans = getPassedList(new Integer[][] {
                { 67, 82, 98, 32, 65, 76, 87, 12, 43, 75, 25 },
                { 42, 90, 80, 12, 76, 58, 95, 30, 67, 78, 10 }
        });
        for (int[] student : ans)
            System.out.print(Arrays.toString(student));
        System.out.println(); // For typesetting

        ans = getPassedList(new Integer[][] {
                { 67, 82, 64, 32, 65, 76 },
                { 42, 90, 80, 12, 76, 58 }
        });
        for (int[] student : ans)
            System.out.print(Arrays.toString(student));
        System.out.println(); // For typesetting
    }
}
