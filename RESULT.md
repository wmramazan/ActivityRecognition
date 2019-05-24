# Results of Classification

| Sensor List       |Values       |
|-------------------|-------------|
|Accelerometer      |x, y, z, a, b|
|Magnetic Field     |x, y, z, a   |
|Gyroscope          |x, y, z      |
|Gravity            |x, y, z      |
|Linear Acceleration|x, y, z      |

```
a = sqrt(x^2 + y^2 + z^2)
b = sqrt(x^2 + z^2) 
```

---

| Feature List      |
|-------------------|
|min|
|max|
|range
|mean|
|quadraticMean|
|std|
|variance|
|medianTop|
|medianMiddle|
|medianBottom|
|kurtosis|
|popVariance|
|skewness|

---

## With Linear Acceleration
| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        4993        |      1417      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         235        |     4 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.6845 %     |         09.3155 %      |
| SMO           |        84.7565 %     |         15.2435 %      |
| J48           |        80.8751 %     |         19.1249 %      |
| Naive Bayes   |        76.1468 %     |         23.8532 %      |

---

## Without Linear Acceleration
| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        4993        |      1417      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     4 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.2611 %     |         09.7389 %      |
| SMO           |        84.1214 %     |         15.8786 %      |
| J48           |        81.5808 %     |         18.4192 %      |
| Naive Bayes   |        76.2879 %     |         23.7121 %      |

---

> Linear acceleration will not be used when obtaining the next results in order to improve performance and consume less energy in smartwatches.

## Other Window Lengths

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        9970        |      2825      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     2 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        87.1504 %     |         12.8496 %      |
| SMO           |        80.7434 %     |         19.2566 %      |
| Naive Bayes   |        71.7168 %     |         28.2832 %      |
| J48           |        70.0531 %     |         29.9469 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        6653        |      1888      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     3 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        89.5127 %     |         10.4873 %      |
| SMO           |        83.5275 %     |         16.4725 %      |
| Naive Bayes   |        74.7352 %     |         25.2648 %      |
| J48           |        70.5508 %     |         29.4492 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3998        |      1135      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     5 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.2203 %     |         09.7797 %      |
| SMO           |        84.5815 %     |         15.4185 %      |
| J48           |        81.4097 %     |         18.5903 %      |
| Naive Bayes   |        77.7093 %     |         22.2907 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3337        |      947      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     6 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        91.5523 %     |         08.4477 %      |
| SMO           |        85.0053 %     |         14.9947 %      |
| Naive Bayes   |        78.5639 %     |         21.4361 %      |
| J48           |        74.5512 %     |         25.4488 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        2862        |      814       |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     7 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.5405 %     |         09.4595 %      |
| SMO           |        85.0123 %     |         14.9877 %      |
| Naive Bayes   |        78.7469 %     |         21.2531 %      |
| J48           |        76.9042 %     |         23.0958 %      |

---

> The window length will be **6 seconds** when obtaining the next results.

## Other Overlapping Values

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        2782        |      789       |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     6 sec     |     40%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.6210 %     |         09.3790 %      |
| SMO           |        86.1850 %     |         13.8150 %      |
| J48           |        82.0025 %     |         17.9975 %      |
| Naive Bayes   |        78.8340 %     |         21.1660 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3036        |      862       |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     6 sec     |     45%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.2552 %     |         09.7448 %      |
| SMO           |        85.0348 %     |         14.9652 %      |
| J48           |        83.0626 %     |         16.9374 %      |
| Naive Bayes   |        78.3063 %     |         21.6937 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3703        |      1052      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     6 sec     |     55%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        91.1597 %     |         08.8403 %      |
| SMO           |        84.9810 %     |         84.9810 %      |
| Naive Bayes   |        79.0875 %     |         20.9125 %      |
| J48           |        74.0494 %     |         25.9506 %      |

---

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        4164        |      1182      |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|         196        |     6 sec     |     60%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        90.0169 %     |         09.9831 %      |
| SMO           |        85.2792 %     |         14.7208 %      |
| J48           |        80.6261 %     |         19.3739 %      |
| Naive Bayes   |        78.6802 %     |         21.3198 %      |

---

> The value of overlapping will be **50%** when obtaining the next results.

## Ranking with Random Forest

```
Ranked attributes:
 1.524444     92 mag_min_z
 1.429583     93 mag_max_z
 1.408581     99 mag_medianTop_z
 1.395154     95 mag_mean_z
 1.390066     57 acc_quadraticMean_b
 1.389725    100 mag_medianMiddle_z
 1.38932     101 mag_medianBottom_z
 1.348982     56 acc_mean_b
 1.329034     74 mag_medianMiddle_x
 1.295113     62 acc_medianBottom_b
 1.288449     75 mag_medianBottom_x
 1.285518     73 mag_medianTop_x
 1.281875     96 mag_quadraticMean_z
 1.2784       69 mag_mean_x
 1.273915     80 mag_max_y
 1.267851     17 acc_mean_y
 1.263173     66 mag_min_x
 1.238711      8 acc_medianTop_x
 1.229148      9 acc_medianMiddle_x
 1.224725     23 acc_medianBottom_y
 1.219576     60 acc_medianTop_b
 1.218673     67 mag_max_x
 1.208073     44 acc_quadraticMean_a
 1.176644    165 gra_medianMiddle_x
 1.165307      4 acc_mean_x
 1.160471    173 gra_mean_y
 1.159373    174 gra_quadraticMean_y
 1.1448      164 gra_medianTop_x
 1.142269     22 acc_medianMiddle_y
 1.139007     21 acc_medianTop_y
 1.134771    166 gra_medianBottom_x
 1.126773     43 acc_mean_a
 1.125821     61 acc_medianMiddle_b
 1.121932     18 acc_quadraticMean_y
 1.112152    179 gra_medianBottom_y
 1.111514    160 gra_mean_x
 1.11053      88 mag_medianBottom_y
 1.108493     10 acc_medianBottom_x
 1.103823     79 mag_min_y
 1.098501    105 mag_min_a
 1.096825     31 acc_quadraticMean_z
 1.095728     54 acc_max_b
 1.094491    171 gra_max_y
 1.093616    114 mag_medianBottom_a
 1.091482    158 gra_max_x
 1.090754      5 acc_quadraticMean_x
 1.089968     34 acc_medianTop_z
 1.08915     122 gyro_quadraticMean_x
 1.087892    127 gyro_medianBottom_x
 1.082995    124 gyro_variance_x
 1.082995    123 gyro_std_x
 1.082995    130 gyro_skewness_x
 1.082995    129 gyro_popVariance_x
 1.082863     30 acc_mean_z
 1.082374    140 gyro_medianBottom_y
 1.077206      1 acc_min_x
 1.076135    113 mag_medianMiddle_a
 1.075883    178 gra_medianMiddle_y
 1.072163      6 acc_std_x
 1.072163      7 acc_variance_x
 1.071852     12 acc_popVariance_x
 1.071852     13 acc_skewness_x
 1.067049    157 gra_min_x
 1.066844    151 gyro_medianTop_z
 1.063035    106 mag_max_a
 1.060145    183 gra_min_z
 1.059553    135 gyro_quadraticMean_y
 1.058515     86 mag_medianTop_y
 1.058039    137 gyro_variance_y
 1.058039    136 gyro_std_y
 1.058039    142 gyro_popVariance_y
 1.058039    143 gyro_skewness_y
 1.053684    112 mag_medianTop_a
 1.046879     35 acc_medianMiddle_z
 1.02666     190 gra_medianTop_z
 1.026317    153 gyro_medianBottom_z
 1.014454     47 acc_medianTop_a
 1.011201    125 gyro_medianTop_x
 1.010948     49 acc_medianBottom_a
 1.007177     82 mag_mean_y
 1.001429    177 gra_medianTop_y
 1.000765     87 mag_medianMiddle_y
 0.999817    109 mag_quadraticMean_a
 0.991896    148 gyro_quadraticMean_z
 0.990665      3 acc_range_x
 0.990488    150 gyro_variance_z
 0.990488    149 gyro_std_z
 0.990488    155 gyro_popVariance_z
 0.990488    156 gyro_skewness_z
 0.987417    108 mag_mean_a
 0.982765     52 acc_skewness_a
 0.982765     51 acc_popVariance_a
 0.982765     45 acc_std_a
 0.982765     46 acc_variance_a
 0.979581     36 acc_medianBottom_z
 0.970334    186 gra_mean_z
 0.968743     83 mag_quadraticMean_y
 0.96825      70 mag_quadraticMean_x
 0.945711    187 gra_quadraticMean_z
 0.939843    191 gra_medianMiddle_z
```


| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3337        |      947       |

| Number of Features   | Window Length | Overlapping |
|:--------------------:|:-------------:|:-----------:|
|10, 20, 30, .. 100       |     6 sec     |     50%     |

| Ranking  | Number of Sensors | Correctly Classified | Incorrectly Classified |
|----------|:-----------------:|:--------------------:|:----------------------:|
| Top 10   |         2         |       61.4572 %      |        38.5428 %       |
| Top 20   |         2         |       68.5322 %      |        31.4678 %       |
| Top 30   |         3         |       74.4456 %      |        25.5544 %       |
| Top 40   |         3         |       81.8374 %      |        18.1626 %       |
| Top 50   |         4         |       87.2228 %      |        12.7772 %       |
| Top 60   |         4         |       89.5459 %      |        10.4541 %       |
| Top 70   |         4         |       90.0739 %      |        09.9261 %       |
| Top 80   |         4         |       90.1795 %      |        09.8205 %       |
| Top 90   |         4         |       90.9187 %      |        09.0813 %       |
| Top 100  |         4         |       91.0243 %      |        08.9757 %       |

---

## Normalized Data with Top 50 of Features

| Training Instances | Test Instances |
|:------------------:|:--------------:|
|        3337        |      947       |

| Number of Features | Window Length | Overlapping |
|:------------------:|:-------------:|:-----------:|
|          50        |     6 sec     |     50%     |

| Approaches    | Correctly Classified | Incorrectly Classified |
|---------------|:--------------------:|:----------------------:|
| Random Forest |        87.5396 %     |         12.4604 %      |
| SMO           |        86.0612 %     |         13.9388 %      |
| J48           |        80.2534 %     |         19.7466 %      |
| Naive Bayes   |        71.4889 %     |         28.5111 %      |

---

## Confusion Matrix with Top 50 of Features and Random Forest

```
   a   b   c   d   e   f   g   h   <-- classified as
  60   0   0   0   0   0   0   0 |   a = UsingComputer
   0 146   0   2   1   0   0   1 |   b = WashingDishes
   0   0   0   0   0   0   0   0 |   c = DrivingCar
   3   7   0 186   2   0   0   2 |   d = EatingMeal
   1  42   0  50  36   0   0   0 |   e = PlayingVideoGame
   0   0   0   1   0 149   0   0 |   f = HavingShower
   2   1   0   0   0   0 155   0 |   g = PlayingGuitar
   0   0   0   3   0   0   0  97 |   h = BrushingTeeth
```
