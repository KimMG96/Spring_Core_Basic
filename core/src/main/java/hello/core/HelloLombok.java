package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.processing.Generated;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setAge(10);
        helloLombok.setName("lomBok");

        System.out.println("helloLombok = " + helloLombok);

    }
}
