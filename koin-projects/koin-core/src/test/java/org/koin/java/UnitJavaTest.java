package org.koin.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.koin.core.KoinApplication;
import org.koin.core.context.GlobalContext;
import org.koin.core.scope.Scope;

import kotlin.Lazy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.koin.core.context.GlobalContext.start;
import static org.koin.core.context.GlobalContextKt.stopKoin;
import static org.koin.core.qualifier.QualifierKt.named;
import static org.koin.java.KoinJavaComponent.get;
import static org.koin.java.KoinJavaComponent.getKoin;
import static org.koin.java.KoinJavaComponent.inject;
import static org.koin.java.UnitJavaStuffKt.koinModule;

public class UnitJavaTest {

    @Before
    public void before() {
        KoinApplication koinApp = KoinApplication
                .init()
                .printLogger()
                .modules(koinModule)
                .create();

        start(koinApp);
    }

    @After
    public void after() {
        stopKoin();
    }

    @Test
    public void successful_get() {
        ComponentA a = get(ComponentA.class);
        assertNotNull(a);

        ComponentB b = get(ComponentB.class);
        assertNotNull(b);

        ComponentC c = get(ComponentC.class);
        assertNotNull(c);

        assertEquals(a, b.getComponentA());
        assertEquals(a, c.getA());
    }

    @Test
    public void successful_lazy() {
        Lazy<ComponentA> lazy_a = inject(ComponentA.class);

        Lazy<ComponentB> lazy_b = inject(ComponentB.class);

        Lazy<ComponentC> lazy_c = inject(ComponentC.class);

        assertEquals(lazy_a.getValue(), lazy_b.getValue().getComponentA());
        assertEquals(lazy_a.getValue(), lazy_c.getValue().getA());

        Scope session = getKoin().createScope("mySession", named("Session"));

        assertNotNull(session.get(ComponentD.class));

        session.close();
        GlobalContext.stop();
    }
}