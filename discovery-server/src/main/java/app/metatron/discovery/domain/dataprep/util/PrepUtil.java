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

package app.metatron.discovery.domain.dataprep.util;

import static app.metatron.discovery.domain.dataprep.exceptions.PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE;
import static app.metatron.discovery.domain.dataprep.exceptions.PrepErrorCodes.PREP_DATASET_ERROR_CODE;
import static app.metatron.discovery.domain.dataprep.exceptions.PrepErrorCodes.PREP_INVALID_CONFIG_CODE;
import static app.metatron.discovery.domain.dataprep.exceptions.PrepErrorCodes.PREP_SNAPSHOT_ERROR_CODE;
import static app.metatron.discovery.domain.dataprep.exceptions.PrepErrorCodes.PREP_TRANSFORM_ERROR_CODE;

import app.metatron.discovery.domain.dataprep.exceptions.PrepException;
import app.metatron.discovery.domain.dataprep.exceptions.PrepMessageKey;
import java.io.File;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public class PrepUtil {

  public static int getLengthUTF8(CharSequence sequence) {
    if (sequence == null) {
      return 0;
    }
    int count = 0;
    for (int i = 0, len = sequence.length(); i < len; i++) {
      char ch = sequence.charAt(i);
      if (ch <= 0x7F) {
        count++;
      } else if (ch <= 0x7FF) {
        count += 2;
      } else if (Character.isHighSurrogate(ch)) {
        count += 4;
        ++i;
      } else {
        count += 3;
      }
    }
    return count;
  }

  public final static double EPSILON = 1.0e-20;

  public static Double round(double val) {
    return Double.valueOf(String.format("%.16f", Math.abs(val) < EPSILON ? 0.0 : val));
  }

  private static boolean existsLocal(String path) {
    File f = new File(path);
    return f.exists();
  }

  public static Configuration getHadoopConf(String hadoopConfDir) {
    if (hadoopConfDir == null) {
      return null;
    }

    String coreSite = hadoopConfDir + File.separator + "core-site.xml";
    String hdfsSite = hadoopConfDir + File.separator + "hdfs-site.xml";

    if (!existsLocal(coreSite)) {
      throw PrepException
              .create(PREP_DATASET_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_HADOOP_CORE_SITE_NOT_FOUND,
                      coreSite);
    }
    if (!existsLocal(hdfsSite)) {
      throw PrepException
              .create(PREP_DATASET_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_HADOOP_HDFS_SITE_NOT_FOUND,
                      hdfsSite);
    }

    Configuration hadoopConf = new Configuration();
    hadoopConf.addResource(new Path(coreSite));
    hadoopConf.addResource(new Path(hdfsSite));

    return hadoopConf;
  }

  public static String getFileNameFromStrUri(String strUri) {
    return strUri.substring(strUri.lastIndexOf('/') + 1, strUri.length());
  }

  public static PrepException datasetError(PrepMessageKey msgKey, String detail) {
    return PrepException.create(PREP_DATASET_ERROR_CODE, msgKey, detail);
  }

  public static PrepException datasetError(PrepMessageKey msgKey) {
    return PrepException.create(PREP_DATASET_ERROR_CODE, msgKey, null);
  }

  public static PrepException datasetError(Exception e) {
    return PrepException.create(PREP_DATASET_ERROR_CODE, e);
  }

  public static PrepException dataflowError(PrepMessageKey msgKey, String detail) {
    return PrepException.create(PREP_DATAFLOW_ERROR_CODE, msgKey, detail);
  }

  public static PrepException dataflowError(PrepMessageKey msgKey) {
    return PrepException.create(PREP_DATAFLOW_ERROR_CODE, msgKey, null);
  }

  public static PrepException dataflowError(Exception e) {
    return PrepException.create(PREP_DATAFLOW_ERROR_CODE, e);
  }

  public static PrepException snapshotError(PrepMessageKey msgKey, String detail) {
    return PrepException.create(PREP_SNAPSHOT_ERROR_CODE, msgKey, detail);
  }

  public static PrepException snapshotError(PrepMessageKey msgKey) {
    return PrepException.create(PREP_SNAPSHOT_ERROR_CODE, msgKey, null);
  }

  public static PrepException snapshotError(Exception e) {
    return PrepException.create(PREP_SNAPSHOT_ERROR_CODE, e);
  }

  public static PrepException configError(PrepMessageKey msgKey, String detail) {
    return PrepException.create(PREP_INVALID_CONFIG_CODE, msgKey, detail);
  }

  public static PrepException configError(PrepMessageKey msgKey) {
    return PrepException.create(PREP_INVALID_CONFIG_CODE, msgKey, null);
  }

  public static PrepException configError(Exception e) {
    return PrepException.create(PREP_INVALID_CONFIG_CODE, e);
  }

  public static PrepException transformError(PrepMessageKey msgKey, String detail) {
    return PrepException.create(PREP_TRANSFORM_ERROR_CODE, msgKey, detail);
  }

  public static PrepException transformError(PrepMessageKey msgKey) {
    return PrepException.create(PREP_TRANSFORM_ERROR_CODE, msgKey, null);
  }

  public static PrepException transformError(Exception e) {
    return PrepException.create(PREP_TRANSFORM_ERROR_CODE, e);
  }
}
