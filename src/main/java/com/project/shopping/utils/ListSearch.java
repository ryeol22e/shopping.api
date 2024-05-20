package com.project.shopping.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListSearch<T> {
    private ListSearch() {}

    /**
     * 원시 데이터 true, false
     * 
     * @param list
     * @param findData
     * @return
     */
    public static boolean isExistItem(List<?> list, Object findData) {
        return findListData(list.stream().sorted().collect(Collectors.toList()), findData);
    }

    /**
     * 객체 데이터 true, false
     * 
     * @param list
     * @param findData
     * @param fieldName
     * @return
     */
    public static boolean isExistItem(List<?> list, Object findData, String fieldName) {
        List<Map<String, Object>> convertList =
                list.stream().map((item) -> convertMap(item)).collect(Collectors.toList());
        List<Map<String, Object>> sortList = convertList.stream()
                .sorted(((Map<String, Object> a,
                        Map<String, Object> b) -> itemCompare(a.get(fieldName), b.get(fieldName))))
                .collect(Collectors.toList());

        return findListData(sortList, findData, fieldName) != null ? true : false;
    }

    /**
     * 원시 데이터 탐색
     * 
     * @param sortList
     * @param findData
     * @return
     */
    private static boolean findListData(List<?> sortList, Object findData) {
        boolean result = false;
        int left = 0;
        int right = sortList.size() - 1;

        for (int i = 0, size = sortList.size(); i < size; i++) {
            int mid = (int) Math.floor((left + right) / 2);
            Object item = sortList.get(mid);

            if (itemCompare(item, findData) == 0) {
                result = true;
                break;
            } else if (itemCompare(item, findData) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    /**
     * 객체 데이터 탐색
     * 
     * @param sortList
     * @param findData
     * @param fieldName
     * @return
     */
    private static Map<String, Object> findListData(List<Map<String, Object>> sortList,
            Object findData, String fieldName) {
        Map<String, Object> result = null;
        int left = 0;
        int right = sortList.size() - 1;

        for (int i = 0, size = sortList.size(); i < size; i++) {
            int mid = (int) Math.floor((left + right) / 2);
            Map<String, Object> item = sortList.get(mid);
            Object value = item.get(fieldName);

            if (itemCompare(value, findData) == 0) {
                result = item;
                break;
            } else if (itemCompare(value, findData) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    /**
     * 검색 데이터 탐색 list
     * 
     * @param sortList
     * @param findData
     * @param fieldName
     * @return
     */
    private static List<Map<String, Object>> findListDataList(List<Map<String, Object>> sortList,
            Object findData, String fieldName) {
        List<Map<String, Object>> result = new ArrayList<>();
        int left = 0;
        int right = sortList.size() - 1;
        int mid = (int) Math.floor((left + right) / 2);
        boolean searchFlag = false;

        for (int i = 0, size = sortList.size(); i < size; i++) {
            if (mid < size) {
                if (!searchFlag) {
                    mid = (int) Math.floor((left + right) / 2);
                }

                Map<String, Object> item = sortList.get(mid);
                Object value = item.get(fieldName);

                if (itemCompare(value, findData) == 0) {
                    result.add(item);
                    mid += 1;
                    searchFlag = true;
                    continue;
                } else if (itemCompare(value, findData) < 0) {
                    left = mid + 1;
                    searchFlag = false;
                } else {
                    right = mid - 1;
                    searchFlag = false;
                }
            }
        }

        return result;
    }

    /**
     * 객체데이터 리스트 start
     * 
     * @param clazz
     * @return
     */
    public static WrapperSearch returnType(Class<?> clazz) {
        return new WrapperSearch(clazz);
    }

    /**
     * 객체 데이터 리스트 search
     */
    public static class WrapperSearch {
        private Class<?> clazz;

        private WrapperSearch(Class<?> clazz) {
            this.clazz = clazz;
        }

        /**
         * 단일 데이터 반환
         * 
         * @param <T>
         * @param list
         * @param findData
         * @param fieldName
         * @return
         */
        public <T> T returnItem(List<?> list, Object findData, String fieldName) {
            List<Map<String, Object>> convertList =
                    list.stream().map((item) -> convertMap(item)).collect(Collectors.toList());
            List<Map<String, Object>> sortList =
                    convertList.stream()
                            .sorted(((Map<String, Object> a, Map<String, Object> b) -> itemCompare(
                                    a.get(fieldName), b.get(fieldName))))
                            .collect(Collectors.toList());

            return convertObject(findListData(sortList, findData, fieldName));
        }

        /**
         * 데이터 리스트 반환
         * 
         * @param <T>
         * @param list
         * @param findData
         * @param fieldName
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T> List<T> returnItemList(List<?> list, Object findData, String fieldName) {
            List<Map<String, Object>> convertList =
                    list.stream().map((item) -> convertMap(item)).collect(Collectors.toList());
            List<Map<String, Object>> sortList =
                    convertList.stream()
                            .sorted(((Map<String, Object> a, Map<String, Object> b) -> itemCompare(
                                    a.get(fieldName), b.get(fieldName))))
                            .collect(Collectors.toList());

            return findListDataList(sortList, findData, fieldName).stream()
                    .map((item) -> (T) convertObject(item)).collect(Collectors.toList());
        }

        /**
         * map > wrapper class convert
         * 
         * @param <T>
         * @param param
         * @return
         */
        @SuppressWarnings("unchecked")
        private <T> T convertObject(Map<String, Object> param) {
            Function<Map<String, Object>, Object> function = (map) -> {
                Object object = null;

                try {
                    object = this.clazz.getConstructor().newInstance();
                    Field[] superFieldArr = object.getClass().getSuperclass().getDeclaredFields();
                    Field[] itemFieldArr = object.getClass().getDeclaredFields();

                    if (superFieldArr.length > 0) {
                        for (int i = 0, size = superFieldArr.length; i < size; i++) {
                            Field field = superFieldArr[i];
                            String fieldName = "";

                            field.setAccessible(true);
                            fieldName = field.getName();

                            if (!fieldName.isEmpty() && map.containsKey(fieldName)) {
                                field.set(object, map.get(fieldName));
                            }
                        }
                    }

                    if (itemFieldArr.length > 0) {
                        for (int i = 0, size = itemFieldArr.length; i < size; i++) {
                            Field field = itemFieldArr[i];
                            String fieldName = "";

                            field.setAccessible(true);
                            fieldName = field.getName();

                            if (!fieldName.isEmpty() && map.containsKey(fieldName)) {
                                field.set(object, map.get(fieldName));
                            }
                        }
                    }
                } catch (Exception e) {
                }

                return object;
            };

            return param != null ? (T) function.apply(param) : null;
        }
    }

    /**
     * wrapper class > map convert
     * 
     * @param param
     * @return
     */
    private static Map<String, Object> convertMap(Object param) {
        Function<Object, Map<String, Object>> function = (item) -> {
            Map<String, Object> map = new HashMap<>();
            Field[] superFieldArr = item.getClass().getSuperclass().getDeclaredFields();
            Field[] itemFieldArr = item.getClass().getDeclaredFields();

            try {
                for (int i = 0, size = superFieldArr.length; i < size; i++) {
                    Field field = superFieldArr[i];
                    Object value = null;

                    field.setAccessible(true);
                    value = field.get((Object) item);

                    if (value == null) {
                        continue;
                    }

                    map.put(field.getName(), value);
                }

                for (int i = 0, size = itemFieldArr.length; i < size; i++) {
                    Field field = itemFieldArr[i];
                    Object value = null;

                    field.setAccessible(true);
                    value = field.get((Object) item);

                    if (value == null) {
                        continue;
                    }

                    map.put(field.getName(), value);
                }
            } catch (Exception e) {
            }

            return map;
        };

        return function.apply(param);
    }

    /**
     * type에 따른 값 비교
     * 
     * @param t1
     * @param t2
     * @return
     */
    private static int itemCompare(Object t1, Object t2) {
        Comparator<Object> comparator = (a, b) -> {
            String aType = a.getClass().getTypeName().toLowerCase();
            String bType = b.getClass().getTypeName().toLowerCase();
            int compare = 0;
            String type = "";

            if (aType.equals(bType)) {
                String typeName = bType.getClass().getTypeName().toLowerCase();
                type = typeName.substring(typeName.lastIndexOf(".") + 1);

                switch (type) {
                    case ("string"):
                        compare = String.valueOf(a).compareTo(String.valueOf(b));
                        break;
                    case "integer":
                        compare = Integer.compare((Integer) a, (Integer) b);
                        break;
                    case "long":
                        compare = Long.compare((Long) a, (Long) b);
                        break;
                    case "float":
                        compare = Float.compare((Float) a, (Float) b);
                        break;
                    case "double":
                        compare = Double.compare((Double) a, (Double) b);
                        break;
                }
            }

            return compare;
        };

        return comparator.compare(t1, t2);
    }
}
