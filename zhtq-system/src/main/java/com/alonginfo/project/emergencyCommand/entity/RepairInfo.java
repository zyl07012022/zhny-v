package com.alonginfo.project.emergencyCommand.entity;

public class RepairInfo {
    /***
     * id
     */
    private String id;
    /**
     * 驻点名称
     */
    private String name;
    /**
     * 驻点地址
     */
    private String address;
    /**
     * 驻点电话
     */
    private String phone;
    /**
     * 驻点负责人
     */
    private String leader;
    /**
     * 驻点职能
     */
    private String vocational_ability;
    /**
     * 所属单位
     */
    private String orgname;

    public RepairInfo() {
    }

    public RepairInfo(String id, String name, String address, String phone, String leader, String vocational_ability, String orgname) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.leader = leader;
        this.vocational_ability = vocational_ability;
        this.orgname = orgname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getVocational_ability() {
        return vocational_ability;
    }

    public void setVocational_ability(String vocational_ability) {
        this.vocational_ability = vocational_ability;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
