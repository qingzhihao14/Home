package com.my.friends.dao;

import java.util.ArrayList;
import java.util.List;

public class LbItemCopyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LbItemCopyExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andParentIsNull() {
            addCriterion("parent is null");
            return (Criteria) this;
        }

        public Criteria andParentIsNotNull() {
            addCriterion("parent is not null");
            return (Criteria) this;
        }

        public Criteria andParentEqualTo(String value) {
            addCriterion("parent =", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentNotEqualTo(String value) {
            addCriterion("parent <>", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentGreaterThan(String value) {
            addCriterion("parent >", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentGreaterThanOrEqualTo(String value) {
            addCriterion("parent >=", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentLessThan(String value) {
            addCriterion("parent <", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentLessThanOrEqualTo(String value) {
            addCriterion("parent <=", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentLike(String value) {
            addCriterion("parent like", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentNotLike(String value) {
            addCriterion("parent not like", value, "parent");
            return (Criteria) this;
        }

        public Criteria andParentIn(List<String> values) {
            addCriterion("parent in", values, "parent");
            return (Criteria) this;
        }

        public Criteria andParentNotIn(List<String> values) {
            addCriterion("parent not in", values, "parent");
            return (Criteria) this;
        }

        public Criteria andParentBetween(String value1, String value2) {
            addCriterion("parent between", value1, value2, "parent");
            return (Criteria) this;
        }

        public Criteria andParentNotBetween(String value1, String value2) {
            addCriterion("parent not between", value1, value2, "parent");
            return (Criteria) this;
        }

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPriceIsNull() {
            addCriterion("price is null");
            return (Criteria) this;
        }

        public Criteria andPriceIsNotNull() {
            addCriterion("price is not null");
            return (Criteria) this;
        }

        public Criteria andPriceEqualTo(Integer value) {
            addCriterion("price =", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotEqualTo(Integer value) {
            addCriterion("price <>", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThan(Integer value) {
            addCriterion("price >", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceGreaterThanOrEqualTo(Integer value) {
            addCriterion("price >=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThan(Integer value) {
            addCriterion("price <", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceLessThanOrEqualTo(Integer value) {
            addCriterion("price <=", value, "price");
            return (Criteria) this;
        }

        public Criteria andPriceIn(List<Integer> values) {
            addCriterion("price in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotIn(List<Integer> values) {
            addCriterion("price not in", values, "price");
            return (Criteria) this;
        }

        public Criteria andPriceBetween(Integer value1, Integer value2) {
            addCriterion("price between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andPriceNotBetween(Integer value1, Integer value2) {
            addCriterion("price not between", value1, value2, "price");
            return (Criteria) this;
        }

        public Criteria andUnitIsNull() {
            addCriterion("unit is null");
            return (Criteria) this;
        }

        public Criteria andUnitIsNotNull() {
            addCriterion("unit is not null");
            return (Criteria) this;
        }

        public Criteria andUnitEqualTo(String value) {
            addCriterion("unit =", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotEqualTo(String value) {
            addCriterion("unit <>", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThan(String value) {
            addCriterion("unit >", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitGreaterThanOrEqualTo(String value) {
            addCriterion("unit >=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThan(String value) {
            addCriterion("unit <", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLessThanOrEqualTo(String value) {
            addCriterion("unit <=", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitLike(String value) {
            addCriterion("unit like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotLike(String value) {
            addCriterion("unit not like", value, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitIn(List<String> values) {
            addCriterion("unit in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotIn(List<String> values) {
            addCriterion("unit not in", values, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitBetween(String value1, String value2) {
            addCriterion("unit between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andUnitNotBetween(String value1, String value2) {
            addCriterion("unit not between", value1, value2, "unit");
            return (Criteria) this;
        }

        public Criteria andSoldIsNull() {
            addCriterion("sold is null");
            return (Criteria) this;
        }

        public Criteria andSoldIsNotNull() {
            addCriterion("sold is not null");
            return (Criteria) this;
        }

        public Criteria andSoldEqualTo(Integer value) {
            addCriterion("sold =", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldNotEqualTo(Integer value) {
            addCriterion("sold <>", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldGreaterThan(Integer value) {
            addCriterion("sold >", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldGreaterThanOrEqualTo(Integer value) {
            addCriterion("sold >=", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldLessThan(Integer value) {
            addCriterion("sold <", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldLessThanOrEqualTo(Integer value) {
            addCriterion("sold <=", value, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldIn(List<Integer> values) {
            addCriterion("sold in", values, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldNotIn(List<Integer> values) {
            addCriterion("sold not in", values, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldBetween(Integer value1, Integer value2) {
            addCriterion("sold between", value1, value2, "sold");
            return (Criteria) this;
        }

        public Criteria andSoldNotBetween(Integer value1, Integer value2) {
            addCriterion("sold not between", value1, value2, "sold");
            return (Criteria) this;
        }

        public Criteria andIschoiceIsNull() {
            addCriterion("ischoice is null");
            return (Criteria) this;
        }

        public Criteria andIschoiceIsNotNull() {
            addCriterion("ischoice is not null");
            return (Criteria) this;
        }

        public Criteria andIschoiceEqualTo(Integer value) {
            addCriterion("ischoice =", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceNotEqualTo(Integer value) {
            addCriterion("ischoice <>", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceGreaterThan(Integer value) {
            addCriterion("ischoice >", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceGreaterThanOrEqualTo(Integer value) {
            addCriterion("ischoice >=", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceLessThan(Integer value) {
            addCriterion("ischoice <", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceLessThanOrEqualTo(Integer value) {
            addCriterion("ischoice <=", value, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceIn(List<Integer> values) {
            addCriterion("ischoice in", values, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceNotIn(List<Integer> values) {
            addCriterion("ischoice not in", values, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceBetween(Integer value1, Integer value2) {
            addCriterion("ischoice between", value1, value2, "ischoice");
            return (Criteria) this;
        }

        public Criteria andIschoiceNotBetween(Integer value1, Integer value2) {
            addCriterion("ischoice not between", value1, value2, "ischoice");
            return (Criteria) this;
        }

        public Criteria andFwqxIsNull() {
            addCriterion("fwqx is null");
            return (Criteria) this;
        }

        public Criteria andFwqxIsNotNull() {
            addCriterion("fwqx is not null");
            return (Criteria) this;
        }

        public Criteria andFwqxEqualTo(String value) {
            addCriterion("fwqx =", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxNotEqualTo(String value) {
            addCriterion("fwqx <>", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxGreaterThan(String value) {
            addCriterion("fwqx >", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxGreaterThanOrEqualTo(String value) {
            addCriterion("fwqx >=", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxLessThan(String value) {
            addCriterion("fwqx <", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxLessThanOrEqualTo(String value) {
            addCriterion("fwqx <=", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxLike(String value) {
            addCriterion("fwqx like", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxNotLike(String value) {
            addCriterion("fwqx not like", value, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxIn(List<String> values) {
            addCriterion("fwqx in", values, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxNotIn(List<String> values) {
            addCriterion("fwqx not in", values, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxBetween(String value1, String value2) {
            addCriterion("fwqx between", value1, value2, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwqxNotBetween(String value1, String value2) {
            addCriterion("fwqx not between", value1, value2, "fwqx");
            return (Criteria) this;
        }

        public Criteria andFwxzIsNull() {
            addCriterion("fwxz is null");
            return (Criteria) this;
        }

        public Criteria andFwxzIsNotNull() {
            addCriterion("fwxz is not null");
            return (Criteria) this;
        }

        public Criteria andFwxzEqualTo(String value) {
            addCriterion("fwxz =", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzNotEqualTo(String value) {
            addCriterion("fwxz <>", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzGreaterThan(String value) {
            addCriterion("fwxz >", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzGreaterThanOrEqualTo(String value) {
            addCriterion("fwxz >=", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzLessThan(String value) {
            addCriterion("fwxz <", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzLessThanOrEqualTo(String value) {
            addCriterion("fwxz <=", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzLike(String value) {
            addCriterion("fwxz like", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzNotLike(String value) {
            addCriterion("fwxz not like", value, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzIn(List<String> values) {
            addCriterion("fwxz in", values, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzNotIn(List<String> values) {
            addCriterion("fwxz not in", values, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzBetween(String value1, String value2) {
            addCriterion("fwxz between", value1, value2, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxzNotBetween(String value1, String value2) {
            addCriterion("fwxz not between", value1, value2, "fwxz");
            return (Criteria) this;
        }

        public Criteria andFwxjIsNull() {
            addCriterion("fwxj is null");
            return (Criteria) this;
        }

        public Criteria andFwxjIsNotNull() {
            addCriterion("fwxj is not null");
            return (Criteria) this;
        }

        public Criteria andFwxjEqualTo(String value) {
            addCriterion("fwxj =", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjNotEqualTo(String value) {
            addCriterion("fwxj <>", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjGreaterThan(String value) {
            addCriterion("fwxj >", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjGreaterThanOrEqualTo(String value) {
            addCriterion("fwxj >=", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjLessThan(String value) {
            addCriterion("fwxj <", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjLessThanOrEqualTo(String value) {
            addCriterion("fwxj <=", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjLike(String value) {
            addCriterion("fwxj like", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjNotLike(String value) {
            addCriterion("fwxj not like", value, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjIn(List<String> values) {
            addCriterion("fwxj in", values, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjNotIn(List<String> values) {
            addCriterion("fwxj not in", values, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjBetween(String value1, String value2) {
            addCriterion("fwxj between", value1, value2, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwxjNotBetween(String value1, String value2) {
            addCriterion("fwxj not between", value1, value2, "fwxj");
            return (Criteria) this;
        }

        public Criteria andFwbzIsNull() {
            addCriterion("fwbz is null");
            return (Criteria) this;
        }

        public Criteria andFwbzIsNotNull() {
            addCriterion("fwbz is not null");
            return (Criteria) this;
        }

        public Criteria andFwbzEqualTo(String value) {
            addCriterion("fwbz =", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzNotEqualTo(String value) {
            addCriterion("fwbz <>", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzGreaterThan(String value) {
            addCriterion("fwbz >", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzGreaterThanOrEqualTo(String value) {
            addCriterion("fwbz >=", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzLessThan(String value) {
            addCriterion("fwbz <", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzLessThanOrEqualTo(String value) {
            addCriterion("fwbz <=", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzLike(String value) {
            addCriterion("fwbz like", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzNotLike(String value) {
            addCriterion("fwbz not like", value, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzIn(List<String> values) {
            addCriterion("fwbz in", values, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzNotIn(List<String> values) {
            addCriterion("fwbz not in", values, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzBetween(String value1, String value2) {
            addCriterion("fwbz between", value1, value2, "fwbz");
            return (Criteria) this;
        }

        public Criteria andFwbzNotBetween(String value1, String value2) {
            addCriterion("fwbz not between", value1, value2, "fwbz");
            return (Criteria) this;
        }

        public Criteria andPicNameIsNull() {
            addCriterion("pic_name is null");
            return (Criteria) this;
        }

        public Criteria andPicNameIsNotNull() {
            addCriterion("pic_name is not null");
            return (Criteria) this;
        }

        public Criteria andPicNameEqualTo(String value) {
            addCriterion("pic_name =", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameNotEqualTo(String value) {
            addCriterion("pic_name <>", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameGreaterThan(String value) {
            addCriterion("pic_name >", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameGreaterThanOrEqualTo(String value) {
            addCriterion("pic_name >=", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameLessThan(String value) {
            addCriterion("pic_name <", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameLessThanOrEqualTo(String value) {
            addCriterion("pic_name <=", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameLike(String value) {
            addCriterion("pic_name like", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameNotLike(String value) {
            addCriterion("pic_name not like", value, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameIn(List<String> values) {
            addCriterion("pic_name in", values, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameNotIn(List<String> values) {
            addCriterion("pic_name not in", values, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameBetween(String value1, String value2) {
            addCriterion("pic_name between", value1, value2, "picName");
            return (Criteria) this;
        }

        public Criteria andPicNameNotBetween(String value1, String value2) {
            addCriterion("pic_name not between", value1, value2, "picName");
            return (Criteria) this;
        }

        public Criteria andPicPathIsNull() {
            addCriterion("pic_path is null");
            return (Criteria) this;
        }

        public Criteria andPicPathIsNotNull() {
            addCriterion("pic_path is not null");
            return (Criteria) this;
        }

        public Criteria andPicPathEqualTo(String value) {
            addCriterion("pic_path =", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathNotEqualTo(String value) {
            addCriterion("pic_path <>", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathGreaterThan(String value) {
            addCriterion("pic_path >", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathGreaterThanOrEqualTo(String value) {
            addCriterion("pic_path >=", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathLessThan(String value) {
            addCriterion("pic_path <", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathLessThanOrEqualTo(String value) {
            addCriterion("pic_path <=", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathLike(String value) {
            addCriterion("pic_path like", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathNotLike(String value) {
            addCriterion("pic_path not like", value, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathIn(List<String> values) {
            addCriterion("pic_path in", values, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathNotIn(List<String> values) {
            addCriterion("pic_path not in", values, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathBetween(String value1, String value2) {
            addCriterion("pic_path between", value1, value2, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicPathNotBetween(String value1, String value2) {
            addCriterion("pic_path not between", value1, value2, "picPath");
            return (Criteria) this;
        }

        public Criteria andPicTypeIsNull() {
            addCriterion("pic_type is null");
            return (Criteria) this;
        }

        public Criteria andPicTypeIsNotNull() {
            addCriterion("pic_type is not null");
            return (Criteria) this;
        }

        public Criteria andPicTypeEqualTo(String value) {
            addCriterion("pic_type =", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotEqualTo(String value) {
            addCriterion("pic_type <>", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeGreaterThan(String value) {
            addCriterion("pic_type >", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeGreaterThanOrEqualTo(String value) {
            addCriterion("pic_type >=", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeLessThan(String value) {
            addCriterion("pic_type <", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeLessThanOrEqualTo(String value) {
            addCriterion("pic_type <=", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeLike(String value) {
            addCriterion("pic_type like", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotLike(String value) {
            addCriterion("pic_type not like", value, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeIn(List<String> values) {
            addCriterion("pic_type in", values, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotIn(List<String> values) {
            addCriterion("pic_type not in", values, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeBetween(String value1, String value2) {
            addCriterion("pic_type between", value1, value2, "picType");
            return (Criteria) this;
        }

        public Criteria andPicTypeNotBetween(String value1, String value2) {
            addCriterion("pic_type not between", value1, value2, "picType");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}