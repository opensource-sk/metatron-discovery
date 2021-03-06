/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.metatron.discovery.domain.dataprep.teddy;

import static org.junit.Assert.assertEquals;

import app.metatron.discovery.domain.dataprep.teddy.exceptions.TeddyException;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * WrangleTest
 */
public class AggregateTest extends TeddyTest {

  @BeforeClass
  public static void setUp() throws Exception {
    loadGridCsv("multi", "teddy/pivot_test_multiple_column.csv");
  }

  @Test
  public void test_aggregate1() throws IOException, TeddyException {
    DataFrame multi = new DataFrame();
    multi.setByGrid(grids.get("multi"));
    multi = prepare_multi(multi);
    multi.show();

    String ruleString = "aggregate value: count(), sum(measure), avg(measure) group: machine_code,module_code";

    DataFrame newDf = apply_rule(multi, ruleString);
    newDf.show();

    ruleString = "sort order: module_code";

    newDf = apply_rule(newDf, ruleString)
    ;
    newDf.show();

    assertEquals(new Long(36), newDf.rows.get(0).get("sum_measure"));
  }

  @Test
  public void test_aggregate2() throws TeddyException {
    DataFrame multi = new DataFrame();
    multi.setByGrid(grids.get("multi"));
    multi = prepare_multi(multi);
    multi.show();

    String ruleString = "aggregate value: count(), sum(measure), avg(measure) group: machine_code,module_code,measure";

    DataFrame newDf = apply_rule(multi, ruleString);
    newDf.show();

    ruleString = "sort order: row_count, module_code";

    newDf = apply_rule(newDf, ruleString);
    newDf.show();

    assertEquals(new Long(30), newDf.rows.get(0).get("sum_measure"));
  }

  @Test
  public void test_aggregate3() throws TeddyException {
    DataFrame multi = new DataFrame();
    multi.setByGrid(grids.get("multi"));
    multi = prepare_multi(multi);
    multi.show();

    String ruleString = "aggregate value: count(), sum(measure), avg(measure)";

    DataFrame newDf = apply_rule(multi, ruleString);
    newDf.show();

    assertEquals(new Long(205), newDf.rows.get(0).get("sum_measure"));
  }
}
