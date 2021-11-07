package com.luxcampus.reflection;

import java.lang.reflect.*;
import java.lang.InstantiationException;

public class ReflectionUtils {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        // Метод принимает класс и возвращает созданный объект этого класса
        System.out.println("Метод принимает класс и возвращает созданный объект этого класса");
        Cat kuzyaCat = new Cat("Kuzya", 13, true);
        Class clazz = kuzyaCat.getClass();
        Object newCat = ReflectionUtils.createNewObject(clazz);
        Method[] methods = kuzyaCat.getClass().getDeclaredMethods();
        System.out.println(newCat);


        // Метод принимает object и вызывает у него все методы без параметров
        System.out.println("Метод принимает object и вызывает у него все методы без параметров");
        callAllMethodsWithoutParameters(kuzyaCat);

        // Метод принимает object и выводит на экран все сигнатуры методов в который есть final
        System.out.println("Метод принимает object и выводит на экран все сигнатуры методов в который есть final");
        printFinalMethodsSignatures(kuzyaCat);

        //Метод принимает Class и выводит все не публичные методы этого класса
        System.out.println("Метод принимает Class и выводит все не публичные методы этого класса");
        printPublicMethods(kuzyaCat);

        //Метод принимает Class и выводит всех предков класса и все интерфейсы которое класс имплементирует
        System.out.println("Метод принимает Class и выводит всех предков класса и все интерфейсы которое класс имплементирует");
        printMethodParentsAndInterfaces(kuzyaCat.getClass());

        //Метод принимает объект и меняет всего его приватные поля на их нулевые значение (null, 0, false etc)
        System.out.println("Метод принимает объект и меняет всего его приватные поля на их нулевые значение (null, 0, false etc)");
        kuzyaCat.getName();
        clearPrivateFields(kuzyaCat);
        kuzyaCat.getName();
    }

    public static Object createNewObject(Class clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor[] newClassConstructor = clazz.getDeclaredConstructors();
        Object newObject = null;
        for (Constructor constructor : newClassConstructor) {
            if (constructor.getParameterCount() == 0) {
                newObject = constructor.newInstance();
            }
        }
        return newObject;
    }

    public static void callAllMethodsWithoutParameters(Object object) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = object.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.getParameterCount() == 0){

                if (!method.canAccess(object)) {
                    method.trySetAccessible();
                }

                method.invoke(object);

            }
        }
    }

    public static void  printFinalMethodsSignatures(Object object){
        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            int modifier = method.getModifiers();
            StringBuilder methodSignature =  new StringBuilder("");

            if (Modifier.isFinal(modifier)) {
                methodSignature.append(method.getName() + "(");
                Class<?>[] parameters = method.getParameterTypes();

                if (parameters.length!=0) {
                    for (Class<?> parameter : parameters) {
                        methodSignature.append(parameter);
                    }
                }
                methodSignature.append(")");

                System.out.println(methodSignature);
            }

        }
    }

    public static void printPublicMethods(Object object){
        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {

            int modifier = method.getModifiers();

            if (!Modifier.isPublic(modifier)) {
                System.out.println(method);
            }

        }
    }

    public static void printMethodParentsAndInterfaces(Class clazz){
        StringBuilder result = new StringBuilder();
        String clazzName = clazz.getName();
        Class parentClass = clazz.getSuperclass();

        result.append(clazzName + " ");

        if (parentClass!=null){
            result.append("extends " + parentClass + "\n");
        }

        Class[] interfaces = clazz.getInterfaces();
        result.append("and implement ");
        for (Class anInterface : interfaces) {
            result.append(anInterface  + " interface ");
        }

        System.out.println(result);

    }

    public static void clearPrivateFields(Object object) throws IllegalAccessException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();


        for (Field field : fields) {
            if (!field.canAccess(object)) {
                field.trySetAccessible();
                if (field.getType().equals(String.class)) {
                    field.set(object, "");
                } else if (field.getType().equals(int.class) || field.getType().equals(long.class)){
                    field.set(object, 0);
                } else if (field.getType().equals(double.class)){
                    field.set(object, 0.0);
                } else if (field.getType().equals(boolean.class)){
                    field.set(object, false);
                } else {
                    field.set(object, null);
                }
            }
        }
    }

}

class Animal{
    Animal(){

    }
}

interface StandartCat{
    private void sleep(){}
}

class Cat extends Animal implements StandartCat{
    private String name = "Cat";
    private int age;
    private boolean gender;

    Cat(){

    };

    Cat(String catName, int catAge, boolean catGender){
        this.name = catName;
        this.age = catAge;
        this.gender = catGender;
    }


    public String getName() {
        System.out.println("My name is " + name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        if (gender) {
            System.out.println("I am a women");
        } else {
            System.out.println("I am a men");
        }
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private final void seyMeow(int i){
        System.out.println("meow");
    }

    private final void sleep(){
        System.out.println("i'm sleeping");
    }
}

