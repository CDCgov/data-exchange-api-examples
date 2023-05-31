## Performance of Upload API Tests

|  File Name | File Size | Upload Duration [ms]  | Upload Duration [s]  | Note |
|---|---|---|---|---|   
|  large_file_95MB.txt  | 95MB | 25205    | 25.205   | |
|  large_file_95MB.txt  | 95MB | 46841    | 46.841   | |
|  large_file_95MB.txt  | 95MB | 24303    | 24.303   | |
|  large_file_95MB.txt  | 95MB | 8878     | 8.878    | |
|  large_file_95MB.txt  | 95MB | 28440    | 28.44   | concurrent upload |
|  large_file_190MB.txt | 190MB | 45984   | 45.984    | |
|  large_file_190MB.txt | 190MB | 70151   | 70.151    | |
|  large_file_190MB.txt | 190MB | 35144   | 35.144    | |
|  large_file_190MB.txt | 190MB | 39293   | 39.293    | |
|  large_file_190MB.txt | 190MB | 52680   | 52.68     | concurrent upload  |
|  large_file_476MB.txt | 476MB | 931273  | 931.273   | not on z-scaler, received response patch 500 once |
|  large_file_476MB.txt | 476MB | 32093   |  32.093   | on z-scaler |
|  large_file_476MB.txt | 476MB | 30174   |  30.174   | on z-scaler |
|  large_file_476MB.txt | 476MB | 91478   |  91.478   | not on on z-scaler |
|  large_file_953MB.txt | 963MB | 261317  | 261.317   | (~4.35 min) on on z-scaler |
|  large_file_4GB.txt   | 4GB   | 2212094 |  2212.094 | (~36 min) on z-scaler, received responses patch 500, head 423, kept resuming to end |
