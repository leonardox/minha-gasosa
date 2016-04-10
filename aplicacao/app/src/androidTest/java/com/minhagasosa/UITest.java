package com.minhagasosa;

import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by elyer on 10/04/2016.
 */
public class UITest extends ActivityInstrumentationTestCase2<MainActivity> {

    public UITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void test_A_AlterarMarca() throws InterruptedException {
        sleep(30000);//esperando 30 segundos para popular o banco
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

        onView(withId(R.id.spinnerMarca)).perform(click());//clica no spinner de marca
        onView(withText("Chery")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_B_AlterarModelo() throws InterruptedException {
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro

        onView(withId(R.id.spinnerModelo)).perform(click());//clica no spinner de modelo
        onView(withText("Vanquish")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_C_AlterarVersao() throws InterruptedException {
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro

        //Alterando para uma marca que possua um carro com varias versões
        onView(withId(R.id.spinnerMarca)).perform(click());//clica no spinner de marca
        onView(withText("Kia")).perform(click());
        onView(withId(R.id.spinnerVersao)).perform(click());//clica no spinner de versao
        onView(withText("EST 2.7")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_D_AdicionarPotencia() throws InterruptedException {
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro

        onView(withId(R.id.spinnerPot)).perform(click());
        onView(withText("1.4")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_E_AlterarMarcaModeloEVersao() throws InterruptedException {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

        onView(withId(R.id.spinnerMarca)).perform(click());//clica no spinner de marca
        onView(withText("Fiat")).perform(click());

        onView(withId(R.id.spinnerModelo)).perform(click());//clica no spinner de modelo
        onView(withText("Doblo")).perform(click());

        onView(withId(R.id.spinnerVersao)).perform(click());//clica no spinner de versao
        onView(withText("ELX 1.4")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_F_AlterarNenhumDado() throws InterruptedException {
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    public void test_F_AlterarTodosOsDados() throws InterruptedException {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.set_car)).perform(click());//Vai para a tela de seleção de carro
        onView(withId(R.id.textView3)).check(matches(isDisplayed()));

        onView(withId(R.id.spinnerMarca)).perform(click());//clica no spinner de marca
        onView(withText("BMW")).perform(click());

        onView(withId(R.id.spinnerModelo)).perform(click());//clica no spinner de modelo
        onView(withText("135i")).perform(click());

        onView(withId(R.id.spinnerVersao)).perform(click());//clica no spinner de versao
        onView(withText("M 3.0 Turbo")).perform(click());

        onView(withId(R.id.spinnerPot)).perform(click());
        onView(withText("1.6")).perform(click());

        sleep(1000);
        onView(withId(R.id.btnPrevisoes)).perform(click());
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    //INICIO DOS TESTES DA ACTIVITY PREVISÕES

    public void test_G_AdicionarPrecoCombustivelSimples() throws InterruptedException {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));

        onView(withId(R.id.textView4)).check(matches(isDisplayed()));//verifica se o texto é mostrado
        onView(withId(R.id.editTextPrice)).perform(typeText("4.00"));//digita o preco do combustivo 1
    }

    public void test_H_AdicionarPrecoCombustivelDuplo() throws InterruptedException {
        onView(withId(R.id.textView)).check(matches(isDisplayed()));

        onView(withId(R.id.checkBox)).perform(click());//clica no chechBox flex

        onView(withId(R.id.textView4)).check(matches(isDisplayed()));//verifica se o texto é mostrado
        onView(withId(R.id.editTextPrice)).perform(typeText("4.00"));//digita o preco do combustivo 1

        onView(withId(R.id.spinner)).perform(click());//clica no spinner de porcetagem de tanque
        onView(withText("15")).perform(click());

        onView(withId(R.id.textView8)).check(matches(isDisplayed()));//verifica se o texto é mostrado
        onView(withId(R.id.editText)).perform(typeText("2.25"));//digita o preco do combustivo 2

        onView(withId(R.id.spinner)).perform(click());//clica no spinner de porcetagem de tanque
        onView(withText("60")).perform(click());
    }
}
