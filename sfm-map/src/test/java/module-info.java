module org.simpleflatmapper.map.test {
        requires org.simpleflatmapper.map;
        requires org.simpleflatmapper.tuple;
        requires junit;
        requires sfm.test;
        requires mockito.core;
        requires joda.time;


        exports org.simpleflatmapper.test.core.mapper;
 }