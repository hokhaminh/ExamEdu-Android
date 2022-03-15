package models;

import java.util.List;

public class ModuleResponse {
    private int totalRecords;
    private List<Module> payload;

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Module> getPayload() {
        return payload;
    }

    public void setPayload(List<Module> payload) {
        this.payload = payload;
    }

    public class Module {
        private  int moduleId;
        private String moduleCode;
        private  String moduleName;
        private String teacherEmail;

        public Module(String moduleCode, String moduleName, String teacherEmail) {
            this.moduleCode = moduleCode;
            this.moduleName = moduleName;
            this.teacherEmail = teacherEmail;
        }

        public Module(int moduleId, String moduleCode, String moduleName, String teacherEmail) {
            this.moduleId = moduleId;
            this.moduleCode = moduleCode;
            this.moduleName = moduleName;
            this.teacherEmail = teacherEmail;
        }

        public int getModuleId() {
            return moduleId;
        }

        public void setModuleId(int moduleId) {
            this.moduleId = moduleId;
        }

        public String getModuleCode() {
            return moduleCode;
        }

        public void setModuleCode(String moduleCode) {
            this.moduleCode = moduleCode;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getTeacherEmail() {
            return teacherEmail;
        }

        public void setTeacherEmail(String teacherEmail) {
            this.teacherEmail = teacherEmail;
        }
    }
}
