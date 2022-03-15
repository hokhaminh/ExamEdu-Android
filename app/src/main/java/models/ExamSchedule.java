package models;

import java.util.List;

public class ExamSchedule {
//    {
//        "totalRecords": 1,
//            "payload": [
//        {
//            "examId": 8,
//                "examName": "PRN211_4",
//                "description": "Progress Test 4",
//                "moduleCode": "JAV211",
//                "examDay": "2022-03-03T08:52:56.843",
//                "password": "",
//                "durationInMinute": 45
//        }
//                      ]
//    }

    private int totalRecords;
    private List<Exam> payload;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Exam> getPayload() {
        return payload;
    }

    public void setPayload(List<Exam> payload) {
        this.payload = payload;
    }

    public class Exam{
        private String examId;
        private String examName;
        private String description;
        private String moduleCode;
        private String examDay;
        private String password;
        private int durationInMinute;

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getExamName() {
            return examName;
        }

        public void setExamName(String examName) {
            this.examName = examName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
        }

        public String getExamDay() {
            return examDay;
        }

        public void setExamDay(String examDay) {
            this.examDay = examDay;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getDurationInMinute() {
            return durationInMinute;
        }

        public void setDurationInMinute(int durationInMinute) {
            this.durationInMinute = durationInMinute;
        }
    }
}
