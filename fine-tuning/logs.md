# fine-tuning

## Run 01

using mistralai/Mistral-7B-v0.3

```
mlx_lm.lora \
--model ./mlx_model \
--train \
--data ./data \
--fine-tune-type lora \
--num-layers 8 \
--iters 300 \
--batch-size 4 \
--val-batches -1 \
--learning-rate 5e-5 \
--steps-per-report 10 \
--steps-per-eval 20 \
--adapter-path ./adapters/adapter.npz \
--save-every 50 \
--test \
--test-batches -1 \

Loading pretrained model
Loading datasets
Training
Trainable parameters: 0.012% (0.852M/7248.024M)
Starting training..., iters: 300
Calculating loss...: 15it [01:00,  4.01s/it]
Iter 1: Val loss 1.591, Val took 60.169s
[WARNING] Some sequences are longer than 2048 tokens. The longest sentence 2496 will be truncated to 2048. Consider pre-splitting your data to save memory.
Iter 10: Train loss 1.320, Learning Rate 5.000e-05, It/sec 0.136, Tokens/sec 173.208, Trained Tokens 12759, Peak mem 26.190 GB
Calculating loss...: 15it [01:00,  4.06s/it]
Iter 20: Val loss 0.930, Val took 60.917s
Iter 20: Train loss 1.201, Learning Rate 5.000e-05, It/sec 0.180, Tokens/sec 195.561, Trained Tokens 23604, Peak mem 26.190 GB
Iter 30: Train loss 1.010, Learning Rate 5.000e-05, It/sec 0.176, Tokens/sec 214.764, Trained Tokens 35822, Peak mem 26.190 GB
Calculating loss...: 15it [01:00,  4.06s/it]
Iter 40: Val loss 0.892, Val took 60.977s
Iter 40: Train loss 0.887, Learning Rate 5.000e-05, It/sec 0.216, Tokens/sec 210.605, Trained Tokens 45577, Peak mem 26.190 GB
Iter 50: Train loss 0.837, Learning Rate 5.000e-05, It/sec 0.225, Tokens/sec 237.824, Trained Tokens 56145, Peak mem 26.190 GB
Iter 50: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000050_adapters.safetensors.
Calculating loss...: 15it [01:01,  4.08s/it]
Iter 60: Val loss 0.842, Val took 61.179s
Iter 60: Train loss 0.842, Learning Rate 5.000e-05, It/sec 0.208, Tokens/sec 234.898, Trained Tokens 67451, Peak mem 26.190 GB
Iter 70: Train loss 0.796, Learning Rate 5.000e-05, It/sec 0.179, Tokens/sec 207.216, Trained Tokens 79000, Peak mem 26.190 GB
Calculating loss...: 15it [01:01,  4.09s/it]
Iter 80: Val loss 0.812, Val took 61.347s
Iter 80: Train loss 0.902, Learning Rate 5.000e-05, It/sec 0.196, Tokens/sec 201.668, Trained Tokens 89268, Peak mem 26.190 GB
Iter 90: Train loss 0.723, Learning Rate 5.000e-05, It/sec 0.210, Tokens/sec 224.979, Trained Tokens 99963, Peak mem 26.190 GB
Calculating loss...: 15it [01:01,  4.10s/it]
Iter 100: Val loss 0.796, Val took 61.450s
Iter 100: Train loss 0.836, Learning Rate 5.000e-05, It/sec 0.281, Tokens/sec 237.102, Trained Tokens 108409, Peak mem 26.190 GB
Iter 100: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000100_adapters.safetensors.
Iter 110: Train loss 0.738, Learning Rate 5.000e-05, It/sec 0.232, Tokens/sec 234.205, Trained Tokens 118515, Peak mem 26.190 GB
Calculating loss...: 15it [01:01,  4.10s/it]
Iter 120: Val loss 0.781, Val took 61.530s
Iter 120: Train loss 0.834, Learning Rate 5.000e-05, It/sec 0.162, Tokens/sec 182.258, Trained Tokens 129770, Peak mem 26.190 GB
Iter 130: Train loss 0.870, Learning Rate 5.000e-05, It/sec 0.169, Tokens/sec 190.521, Trained Tokens 141052, Peak mem 26.190 GB
Calculating loss...: 15it [01:01,  4.11s/it]
Iter 140: Val loss 0.766, Val took 61.589s
Iter 140: Train loss 0.569, Learning Rate 5.000e-05, It/sec 0.234, Tokens/sec 245.554, Trained Tokens 151564, Peak mem 26.190 GB
Iter 150: Train loss 0.811, Learning Rate 5.000e-05, It/sec 0.212, Tokens/sec 234.212, Trained Tokens 162632, Peak mem 26.190 GB
Iter 150: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000150_adapters.safetensors.
Calculating loss...: 15it [01:01,  4.11s/it]
Iter 160: Val loss 0.755, Val took 61.619s
Iter 160: Train loss 0.725, Learning Rate 5.000e-05, It/sec 0.203, Tokens/sec 203.054, Trained Tokens 172639, Peak mem 26.190 GB
Iter 170: Train loss 0.712, Learning Rate 5.000e-05, It/sec 0.189, Tokens/sec 208.418, Trained Tokens 183688, Peak mem 26.190 GB
Calculating loss...: 15it [01:02,  4.13s/it]
Iter 180: Val loss 0.746, Val took 62.019s
Iter 180: Train loss 0.696, Learning Rate 5.000e-05, It/sec 0.177, Tokens/sec 213.534, Trained Tokens 195761, Peak mem 26.190 GB
Iter 190: Train loss 0.827, Learning Rate 5.000e-05, It/sec 0.198, Tokens/sec 221.657, Trained Tokens 206940, Peak mem 26.190 GB
Calculating loss...: 15it [01:02,  4.14s/it]
Iter 200: Val loss 0.739, Val took 62.052s
Iter 200: Train loss 0.637, Learning Rate 5.000e-05, It/sec 0.247, Tokens/sec 211.298, Trained Tokens 215485, Peak mem 26.190 GB
Iter 200: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000200_adapters.safetensors.
[WARNING] Some sequences are longer than 2048 tokens. The longest sentence 2496 will be truncated to 2048. Consider pre-splitting your data to save memory.
Iter 210: Train loss 0.715, Learning Rate 5.000e-05, It/sec 0.139, Tokens/sec 165.102, Trained Tokens 227349, Peak mem 26.194 GB
Calculating loss...: 15it [01:02,  4.13s/it]
Iter 220: Val loss 0.732, Val took 62.006s
Iter 220: Train loss 0.681, Learning Rate 5.000e-05, It/sec 0.211, Tokens/sec 217.857, Trained Tokens 237698, Peak mem 26.194 GB
Iter 230: Train loss 0.674, Learning Rate 5.000e-05, It/sec 0.190, Tokens/sec 215.195, Trained Tokens 249012, Peak mem 26.194 GB
Calculating loss...: 15it [01:02,  4.13s/it]
Iter 240: Val loss 0.727, Val took 62.002s
Iter 240: Train loss 0.751, Learning Rate 5.000e-05, It/sec 0.188, Tokens/sec 197.616, Trained Tokens 259540, Peak mem 26.194 GB
Iter 250: Train loss 0.717, Learning Rate 5.000e-05, It/sec 0.189, Tokens/sec 224.944, Trained Tokens 271431, Peak mem 26.194 GB
Iter 250: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000250_adapters.safetensors.
Calculating loss...: 15it [01:01,  4.13s/it]
Iter 260: Val loss 0.731, Val took 61.912s
Iter 260: Train loss 0.601, Learning Rate 5.000e-05, It/sec 0.196, Tokens/sec 208.668, Trained Tokens 282051, Peak mem 26.194 GB
Iter 270: Train loss 0.645, Learning Rate 5.000e-05, It/sec 0.218, Tokens/sec 220.252, Trained Tokens 292163, Peak mem 26.194 GB
Calculating loss...: 15it [01:01,  4.12s/it]
Iter 280: Val loss 0.737, Val took 61.735s
Iter 280: Train loss 0.661, Learning Rate 5.000e-05, It/sec 0.185, Tokens/sec 177.891, Trained Tokens 301794, Peak mem 26.194 GB
Iter 290: Train loss 0.594, Learning Rate 5.000e-05, It/sec 0.205, Tokens/sec 207.023, Trained Tokens 311880, Peak mem 26.194 GB
Calculating loss...: 15it [01:02,  4.15s/it]
Iter 300: Val loss 0.727, Val took 62.181s
Iter 300: Train loss 0.648, Learning Rate 5.000e-05, It/sec 0.222, Tokens/sec 227.254, Trained Tokens 322121, Peak mem 26.194 GB
Iter 300: Saved adapter weights to adapters/adapter.npz/adapters.safetensors and adapters/adapter.npz/0000300_adapters.safetensors.
Saved final weights to adapters/adapter.npz/adapters.safetensors.
Testing
Calculating loss...: 15it [01:04,  4.29s/it]
Test loss 0.778, Test ppl 2.177.
```

## Run 02

using mistralai/Mistral-7B-v0.3

```
 mlx_lm.lora \
   --model ./mlx_model \
   --train \
   --data ./data/destillation \
   --fine-tune-type lora \
   --num-layers 8 \
   --iters 800 \
   --batch-size 4 \
   --val-batches -1 \
   --learning-rate 2e-5 \
   --steps-per-report 10 \
   --steps-per-eval 100 \
   --adapter-path ./adapters/02 \
   --save-every 100 \
   --test \
   --test-batches -1 

Loading pretrained model
Loading datasets
Training
Trainable parameters: 0.012% (0.852M/7248.024M)
Starting training..., iters: 800
Calculating loss...: 100it [05:42,  3.43s/it]
Iter 1: Val loss 1.781, Val took 342.602s
Iter 10: Train loss 1.545, Learning Rate 2.000e-05, It/sec 0.221, Tokens/sec 230.639, Trained Tokens 10444, Peak mem 8.839 GB
Iter 20: Train loss 1.237, Learning Rate 2.000e-05, It/sec 0.253, Tokens/sec 236.273, Trained Tokens 19780, Peak mem 8.839 GB
Iter 30: Train loss 0.963, Learning Rate 2.000e-05, It/sec 0.217, Tokens/sec 217.562, Trained Tokens 29805, Peak mem 8.839 GB
Iter 40: Train loss 0.861, Learning Rate 2.000e-05, It/sec 0.260, Tokens/sec 243.412, Trained Tokens 39153, Peak mem 8.839 GB
Iter 50: Train loss 0.917, Learning Rate 2.000e-05, It/sec 0.248, Tokens/sec 231.597, Trained Tokens 48477, Peak mem 8.839 GB
Iter 60: Train loss 0.768, Learning Rate 2.000e-05, It/sec 0.263, Tokens/sec 248.740, Trained Tokens 57940, Peak mem 8.839 GB
Iter 70: Train loss 0.758, Learning Rate 2.000e-05, It/sec 0.255, Tokens/sec 251.719, Trained Tokens 67810, Peak mem 8.839 GB
Iter 80: Train loss 0.937, Learning Rate 2.000e-05, It/sec 0.255, Tokens/sec 237.210, Trained Tokens 77112, Peak mem 8.839 GB
Iter 90: Train loss 0.785, Learning Rate 2.000e-05, It/sec 0.224, Tokens/sec 239.109, Trained Tokens 87779, Peak mem 8.839 GB
Calculating loss...: 100it [05:45,  3.46s/it]
Iter 100: Val loss 0.823, Val took 345.634s
Iter 100: Train loss 0.726, Learning Rate 2.000e-05, It/sec 0.232, Tokens/sec 258.856, Trained Tokens 98936, Peak mem 8.839 GB
Iter 110: Train loss 0.773, Learning Rate 2.000e-05, It/sec 0.242, Tokens/sec 223.193, Trained Tokens 108148, Peak mem 8.839 GB
Iter 120: Train loss 0.871, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 195.211, Trained Tokens 118914, Peak mem 18.845 GB
Iter 130: Train loss 0.825, Learning Rate 2.000e-05, It/sec 0.206, Tokens/sec 209.177, Trained Tokens 129091, Peak mem 18.845 GB
Iter 140: Train loss 0.857, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 207.874, Trained Tokens 139816, Peak mem 18.845 GB
Iter 150: Train loss 0.730, Learning Rate 2.000e-05, It/sec 0.261, Tokens/sec 249.534, Trained Tokens 149369, Peak mem 18.845 GB
Iter 160: Train loss 0.815, Learning Rate 2.000e-05, It/sec 0.234, Tokens/sec 232.482, Trained Tokens 159285, Peak mem 18.845 GB
Iter 170: Train loss 0.730, Learning Rate 2.000e-05, It/sec 0.217, Tokens/sec 212.644, Trained Tokens 169062, Peak mem 18.845 GB
Iter 180: Train loss 0.780, Learning Rate 2.000e-05, It/sec 0.230, Tokens/sec 227.023, Trained Tokens 178917, Peak mem 18.845 GB
Iter 190: Train loss 0.767, Learning Rate 2.000e-05, It/sec 0.199, Tokens/sec 193.906, Trained Tokens 188682, Peak mem 18.845 GB
Calculating loss...: 100it [05:42,  3.42s/it]
Iter 200: Val loss 0.754, Val took 342.205s
Iter 200: Train loss 0.847, Learning Rate 2.000e-05, It/sec 0.198, Tokens/sec 229.607, Trained Tokens 200265, Peak mem 18.845 GB
Iter 200: Saved adapter weights to adapters/02/adapters.safetensors and adapters/02/0000200_adapters.safetensors.
Iter 210: Train loss 0.775, Learning Rate 2.000e-05, It/sec 0.239, Tokens/sec 231.445, Trained Tokens 209953, Peak mem 18.845 GB
Iter 220: Train loss 0.661, Learning Rate 2.000e-05, It/sec 0.246, Tokens/sec 247.197, Trained Tokens 220020, Peak mem 18.845 GB
Iter 230: Train loss 0.728, Learning Rate 2.000e-05, It/sec 0.234, Tokens/sec 217.156, Trained Tokens 229289, Peak mem 18.845 GB
Iter 240: Train loss 0.720, Learning Rate 2.000e-05, It/sec 0.217, Tokens/sec 223.861, Trained Tokens 239596, Peak mem 18.845 GB
Iter 250: Train loss 0.572, Learning Rate 2.000e-05, It/sec 0.280, Tokens/sec 259.040, Trained Tokens 248862, Peak mem 18.845 GB
Iter 260: Train loss 0.719, Learning Rate 2.000e-05, It/sec 0.228, Tokens/sec 223.294, Trained Tokens 258670, Peak mem 18.845 GB
Iter 270: Train loss 0.718, Learning Rate 2.000e-05, It/sec 0.257, Tokens/sec 244.206, Trained Tokens 268172, Peak mem 18.845 GB
Iter 280: Train loss 0.718, Learning Rate 2.000e-05, It/sec 0.224, Tokens/sec 224.821, Trained Tokens 278201, Peak mem 18.845 GB
Iter 290: Train loss 0.711, Learning Rate 2.000e-05, It/sec 0.208, Tokens/sec 225.668, Trained Tokens 289039, Peak mem 18.845 GB
Calculating loss...: 100it [05:42,  3.42s/it]
Iter 300: Val loss 0.716, Val took 342.480s
Iter 300: Train loss 0.742, Learning Rate 2.000e-05, It/sec 0.226, Tokens/sec 225.230, Trained Tokens 299009, Peak mem 18.845 GB
Iter 310: Train loss 0.846, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 207.523, Trained Tokens 309716, Peak mem 18.845 GB
Iter 320: Train loss 0.657, Learning Rate 2.000e-05, It/sec 0.179, Tokens/sec 215.237, Trained Tokens 321724, Peak mem 18.845 GB
```

## Run 03

using mistralai/Mistral-7B-Instruct-v0.3

```
 mlx_lm.lora \
   --model ./mlx_model \
   --train \
   --data ./data/destillation \
   --fine-tune-type lora \
   --num-layers 8 \
   --iters 800 \
   --batch-size 4 \
   --val-batches -1 \
   --learning-rate 1e-5 \
   --steps-per-report 10 \
   --steps-per-eval 100 \
   --adapter-path ./adapters/03 \
   --save-every 100 \
   --test \
   --test-batches -1 
   
Loading datasets
Training
Trainable parameters: 0.012% (0.852M/7248.024M)
Starting training..., iters: 800
Calculating loss...: 100it [05:50,  3.50s/it]
Iter 1: Val loss 2.049, Val took 350.181s
Iter 10: Train loss 1.725, Learning Rate 1.000e-05, It/sec 0.215, Tokens/sec 224.185, Trained Tokens 10444, Peak mem 8.839 GB
Iter 20: Train loss 1.505, Learning Rate 1.000e-05, It/sec 0.245, Tokens/sec 228.842, Trained Tokens 19780, Peak mem 8.839 GB
Iter 30: Train loss 1.162, Learning Rate 1.000e-05, It/sec 0.210, Tokens/sec 210.944, Trained Tokens 29805, Peak mem 8.839 GB
Iter 40: Train loss 1.012, Learning Rate 1.000e-05, It/sec 0.255, Tokens/sec 238.557, Trained Tokens 39153, Peak mem 8.839 GB
Iter 50: Train loss 1.048, Learning Rate 1.000e-05, It/sec 0.242, Tokens/sec 225.825, Trained Tokens 48477, Peak mem 8.839 GB
Iter 60: Train loss 0.886, Learning Rate 1.000e-05, It/sec 0.257, Tokens/sec 243.307, Trained Tokens 57940, Peak mem 8.839 GB
Iter 70: Train loss 0.864, Learning Rate 1.000e-05, It/sec 0.248, Tokens/sec 245.257, Trained Tokens 67810, Peak mem 8.839 GB
Iter 80: Train loss 1.037, Learning Rate 1.000e-05, It/sec 0.249, Tokens/sec 231.723, Trained Tokens 77112, Peak mem 8.839 GB
Iter 90: Train loss 0.866, Learning Rate 1.000e-05, It/sec 0.219, Tokens/sec 233.613, Trained Tokens 87779, Peak mem 8.839 GB
Calculating loss...: 100it [05:53,  3.53s/it]
Iter 100: Val loss 0.904, Val took 353.198s
Iter 100: Train loss 0.822, Learning Rate 1.000e-05, It/sec 0.227, Tokens/sec 253.231, Trained Tokens 98936, Peak mem 8.839 GB
Iter 100: Saved adapter weights to adapters/03/adapters.safetensors and adapters/03/0000100_adapters.safetensors.
Iter 110: Train loss 0.858, Learning Rate 1.000e-05, It/sec 0.237, Tokens/sec 217.934, Trained Tokens 108148, Peak mem 8.839 GB
Iter 120: Train loss 0.957, Learning Rate 1.000e-05, It/sec 0.177, Tokens/sec 190.735, Trained Tokens 118914, Peak mem 18.848 GB
Iter 130: Train loss 0.926, Learning Rate 1.000e-05, It/sec 0.201, Tokens/sec 204.679, Trained Tokens 129091, Peak mem 18.848 GB
Iter 140: Train loss 0.932, Learning Rate 1.000e-05, It/sec 0.181, Tokens/sec 194.006, Trained Tokens 139816, Peak mem 18.848 GB
Iter 150: Train loss 0.808, Learning Rate 1.000e-05, It/sec 0.255, Tokens/sec 243.162, Trained Tokens 149369, Peak mem 18.848 GB
Iter 160: Train loss 0.902, Learning Rate 1.000e-05, It/sec 0.223, Tokens/sec 220.660, Trained Tokens 159285, Peak mem 18.848 GB
Iter 170: Train loss 0.820, Learning Rate 1.000e-05, It/sec 0.208, Tokens/sec 203.614, Trained Tokens 169062, Peak mem 18.848 GB
Iter 180: Train loss 0.867, Learning Rate 1.000e-05, It/sec 0.223, Tokens/sec 219.358, Trained Tokens 178917, Peak mem 18.848 GB
Iter 190: Train loss 0.860, Learning Rate 1.000e-05, It/sec 0.187, Tokens/sec 182.831, Trained Tokens 188682, Peak mem 18.848 GB
```

--> Learning Rate war bei 2e-5 deutlich besser, deshalb Abbruch und neuer Start

## Run 04

using mistralai/Mistral-7B-Instruct-v0.3

```
 mlx_lm.lora \
   --model ./mlx_model \
   --train \
   --data ./data/destillation \
   --fine-tune-type lora \
   --num-layers 8 \
   --iters 1600 \
   --batch-size 4 \
   --val-batches -1 \
   --learning-rate 2e-5 \
   --steps-per-report 10 \
   --steps-per-eval 100 \
   --adapter-path ./adapters/05 \
   --save-every 100 \
   --test \
   --test-batches -1 
```

## Run 05

using mistralai/Mistral-7B-Instruct-v0.3

```
mlx_lm.lora \     
   --model ./mlx_model \
   --train \
   --data ./data/destillation \
   --fine-tune-type lora \
   --num-layers 16 \
   --iters 1600 \
   --batch-size 4 \
   --val-batches -1 \
   --learning-rate 2e-5 \
   --steps-per-report 10 \
   --steps-per-eval 100 \
   --adapter-path ./adapters/05 \
   --save-every 100 \
   --test \
   --test-batches -1
Loading pretrained model
Loading datasets
Training
Trainable parameters: 0.024% (1.704M/7248.024M)
Starting training..., iters: 1600
Calculating loss...: 100it [05:45,  3.45s/it]
Iter 1: Val loss 2.049, Val took 345.161s
Iter 10: Train loss 1.459, Learning Rate 2.000e-05, It/sec 0.182, Tokens/sec 190.022, Trained Tokens 10444, Peak mem 12.478 GB
Iter 20: Train loss 1.185, Learning Rate 2.000e-05, It/sec 0.209, Tokens/sec 195.013, Trained Tokens 19780, Peak mem 12.478 GB
Iter 30: Train loss 0.945, Learning Rate 2.000e-05, It/sec 0.179, Tokens/sec 179.439, Trained Tokens 29805, Peak mem 12.478 GB
Iter 40: Train loss 0.838, Learning Rate 2.000e-05, It/sec 0.215, Tokens/sec 201.209, Trained Tokens 39153, Peak mem 12.478 GB
Iter 50: Train loss 0.894, Learning Rate 2.000e-05, It/sec 0.205, Tokens/sec 191.046, Trained Tokens 48477, Peak mem 12.478 GB
Iter 60: Train loss 0.746, Learning Rate 2.000e-05, It/sec 0.217, Tokens/sec 205.405, Trained Tokens 57940, Peak mem 12.478 GB
Iter 70: Train loss 0.734, Learning Rate 2.000e-05, It/sec 0.211, Tokens/sec 207.848, Trained Tokens 67810, Peak mem 12.478 GB
Iter 80: Train loss 0.909, Learning Rate 2.000e-05, It/sec 0.211, Tokens/sec 195.919, Trained Tokens 77112, Peak mem 12.478 GB
Iter 90: Train loss 0.765, Learning Rate 2.000e-05, It/sec 0.185, Tokens/sec 197.384, Trained Tokens 87779, Peak mem 12.478 GB
Calculating loss...: 100it [05:46,  3.47s/it]
Iter 100: Val loss 0.806, Val took 346.686s
Iter 100: Train loss 0.697, Learning Rate 2.000e-05, It/sec 0.191, Tokens/sec 213.646, Trained Tokens 98936, Peak mem 12.478 GB
Iter 100: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000100_adapters.safetensors.
Iter 110: Train loss 0.759, Learning Rate 2.000e-05, It/sec 0.200, Tokens/sec 184.374, Trained Tokens 108148, Peak mem 12.478 GB
Iter 120: Train loss 0.849, Learning Rate 2.000e-05, It/sec 0.150, Tokens/sec 161.353, Trained Tokens 118914, Peak mem 30.844 GB
Iter 130: Train loss 0.801, Learning Rate 2.000e-05, It/sec 0.170, Tokens/sec 172.912, Trained Tokens 129091, Peak mem 30.844 GB
Iter 140: Train loss 0.850, Learning Rate 2.000e-05, It/sec 0.162, Tokens/sec 173.548, Trained Tokens 139816, Peak mem 30.844 GB
Iter 150: Train loss 0.706, Learning Rate 2.000e-05, It/sec 0.216, Tokens/sec 206.227, Trained Tokens 149369, Peak mem 30.844 GB
Iter 160: Train loss 0.810, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 187.153, Trained Tokens 159285, Peak mem 30.844 GB
Iter 170: Train loss 0.709, Learning Rate 2.000e-05, It/sec 0.177, Tokens/sec 172.667, Trained Tokens 169062, Peak mem 30.844 GB
Iter 180: Train loss 0.786, Learning Rate 2.000e-05, It/sec 0.188, Tokens/sec 185.742, Trained Tokens 178917, Peak mem 30.844 GB
Iter 190: Train loss 0.763, Learning Rate 2.000e-05, It/sec 0.163, Tokens/sec 159.002, Trained Tokens 188682, Peak mem 30.844 GB
Calculating loss...: 100it [05:48,  3.48s/it]
Iter 200: Val loss 0.745, Val took 348.399s
Iter 200: Train loss 0.840, Learning Rate 2.000e-05, It/sec 0.163, Tokens/sec 188.920, Trained Tokens 200265, Peak mem 30.844 GB
Iter 200: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000200_adapters.safetensors.
Iter 210: Train loss 0.774, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 187.581, Trained Tokens 209953, Peak mem 30.844 GB
Iter 220: Train loss 0.650, Learning Rate 2.000e-05, It/sec 0.201, Tokens/sec 202.552, Trained Tokens 220020, Peak mem 30.844 GB
Iter 230: Train loss 0.711, Learning Rate 2.000e-05, It/sec 0.192, Tokens/sec 178.318, Trained Tokens 229289, Peak mem 30.844 GB
Iter 240: Train loss 0.724, Learning Rate 2.000e-05, It/sec 0.178, Tokens/sec 183.671, Trained Tokens 239596, Peak mem 30.844 GB
Iter 250: Train loss 0.573, Learning Rate 2.000e-05, It/sec 0.230, Tokens/sec 212.661, Trained Tokens 248862, Peak mem 30.844 GB
Iter 260: Train loss 0.717, Learning Rate 2.000e-05, It/sec 0.187, Tokens/sec 183.102, Trained Tokens 258670, Peak mem 30.844 GB
Iter 270: Train loss 0.723, Learning Rate 2.000e-05, It/sec 0.211, Tokens/sec 200.458, Trained Tokens 268172, Peak mem 30.844 GB
Iter 280: Train loss 0.717, Learning Rate 2.000e-05, It/sec 0.184, Tokens/sec 184.670, Trained Tokens 278201, Peak mem 30.844 GB
Iter 290: Train loss 0.694, Learning Rate 2.000e-05, It/sec 0.171, Tokens/sec 185.151, Trained Tokens 289039, Peak mem 30.844 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 300: Val loss 0.711, Val took 347.590s
Iter 300: Train loss 0.727, Learning Rate 2.000e-05, It/sec 0.185, Tokens/sec 184.831, Trained Tokens 299009, Peak mem 30.844 GB
Iter 300: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000300_adapters.safetensors.
Iter 310: Train loss 0.838, Learning Rate 2.000e-05, It/sec 0.159, Tokens/sec 170.674, Trained Tokens 309716, Peak mem 30.844 GB
Iter 320: Train loss 0.642, Learning Rate 2.000e-05, It/sec 0.148, Tokens/sec 177.304, Trained Tokens 321724, Peak mem 30.844 GB
Iter 330: Train loss 0.653, Learning Rate 2.000e-05, It/sec 0.191, Tokens/sec 194.617, Trained Tokens 331930, Peak mem 30.844 GB
Iter 340: Train loss 0.573, Learning Rate 2.000e-05, It/sec 0.160, Tokens/sec 168.798, Trained Tokens 342508, Peak mem 30.844 GB
Iter 350: Train loss 0.703, Learning Rate 2.000e-05, It/sec 0.167, Tokens/sec 164.832, Trained Tokens 352354, Peak mem 30.844 GB
Iter 360: Train loss 0.615, Learning Rate 2.000e-05, It/sec 0.187, Tokens/sec 183.717, Trained Tokens 362182, Peak mem 30.844 GB
Iter 370: Train loss 0.783, Learning Rate 2.000e-05, It/sec 0.196, Tokens/sec 185.252, Trained Tokens 371635, Peak mem 30.844 GB
Iter 380: Train loss 0.715, Learning Rate 2.000e-05, It/sec 0.130, Tokens/sec 154.431, Trained Tokens 383500, Peak mem 33.246 GB
Iter 390: Train loss 0.664, Learning Rate 2.000e-05, It/sec 0.225, Tokens/sec 205.351, Trained Tokens 392642, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 400: Val loss 0.682, Val took 347.577s
Iter 400: Train loss 0.589, Learning Rate 2.000e-05, It/sec 0.212, Tokens/sec 200.703, Trained Tokens 402123, Peak mem 33.246 GB
Iter 400: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000400_adapters.safetensors.
Iter 410: Train loss 0.720, Learning Rate 2.000e-05, It/sec 0.237, Tokens/sec 190.224, Trained Tokens 410140, Peak mem 33.246 GB
Iter 420: Train loss 0.645, Learning Rate 2.000e-05, It/sec 0.212, Tokens/sec 197.331, Trained Tokens 419464, Peak mem 33.246 GB
Iter 430: Train loss 0.595, Learning Rate 2.000e-05, It/sec 0.208, Tokens/sec 194.096, Trained Tokens 428798, Peak mem 33.246 GB
Iter 440: Train loss 0.712, Learning Rate 2.000e-05, It/sec 0.204, Tokens/sec 199.518, Trained Tokens 438588, Peak mem 33.246 GB
Iter 450: Train loss 0.752, Learning Rate 2.000e-05, It/sec 0.175, Tokens/sec 177.241, Trained Tokens 448720, Peak mem 33.246 GB
Iter 460: Train loss 0.773, Learning Rate 2.000e-05, It/sec 0.156, Tokens/sec 160.883, Trained Tokens 459063, Peak mem 33.246 GB
Iter 470: Train loss 0.723, Learning Rate 2.000e-05, It/sec 0.213, Tokens/sec 185.013, Trained Tokens 467741, Peak mem 33.246 GB
Iter 480: Train loss 0.683, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 182.216, Trained Tokens 477370, Peak mem 33.246 GB
Iter 490: Train loss 0.544, Learning Rate 2.000e-05, It/sec 0.177, Tokens/sec 178.054, Trained Tokens 487417, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 500: Val loss 0.664, Val took 347.546s
Iter 500: Train loss 0.672, Learning Rate 2.000e-05, It/sec 0.182, Tokens/sec 180.912, Trained Tokens 497346, Peak mem 33.246 GB
Iter 500: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000500_adapters.safetensors.
Iter 510: Train loss 0.693, Learning Rate 2.000e-05, It/sec 0.204, Tokens/sec 189.606, Trained Tokens 506657, Peak mem 33.246 GB
Iter 520: Train loss 0.638, Learning Rate 2.000e-05, It/sec 0.218, Tokens/sec 190.879, Trained Tokens 515400, Peak mem 33.246 GB
Iter 530: Train loss 0.722, Learning Rate 2.000e-05, It/sec 0.205, Tokens/sec 200.643, Trained Tokens 525166, Peak mem 33.246 GB
Iter 540: Train loss 0.643, Learning Rate 2.000e-05, It/sec 0.204, Tokens/sec 191.221, Trained Tokens 534562, Peak mem 33.246 GB
Iter 550: Train loss 0.676, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 190.588, Trained Tokens 544376, Peak mem 33.246 GB
Iter 560: Train loss 0.579, Learning Rate 2.000e-05, It/sec 0.157, Tokens/sec 166.009, Trained Tokens 554956, Peak mem 33.246 GB
Iter 570: Train loss 0.701, Learning Rate 2.000e-05, It/sec 0.179, Tokens/sec 166.886, Trained Tokens 564292, Peak mem 33.246 GB
Iter 580: Train loss 0.683, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 186.756, Trained Tokens 574582, Peak mem 33.246 GB
Iter 590: Train loss 0.542, Learning Rate 2.000e-05, It/sec 0.196, Tokens/sec 190.318, Trained Tokens 584278, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 600: Val loss 0.655, Val took 347.745s
Iter 600: Train loss 0.619, Learning Rate 2.000e-05, It/sec 0.154, Tokens/sec 184.831, Trained Tokens 596315, Peak mem 33.246 GB
Iter 600: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000600_adapters.safetensors.
Iter 610: Train loss 0.653, Learning Rate 2.000e-05, It/sec 0.174, Tokens/sec 189.071, Trained Tokens 607191, Peak mem 33.246 GB
Iter 620: Train loss 0.561, Learning Rate 2.000e-05, It/sec 0.204, Tokens/sec 198.711, Trained Tokens 616950, Peak mem 33.246 GB
Iter 630: Train loss 0.658, Learning Rate 2.000e-05, It/sec 0.159, Tokens/sec 159.454, Trained Tokens 626980, Peak mem 33.246 GB
Iter 640: Train loss 0.635, Learning Rate 2.000e-05, It/sec 0.210, Tokens/sec 199.100, Trained Tokens 636480, Peak mem 33.246 GB
Iter 650: Train loss 0.647, Learning Rate 2.000e-05, It/sec 0.232, Tokens/sec 210.508, Trained Tokens 645554, Peak mem 33.246 GB
Iter 660: Train loss 0.685, Learning Rate 2.000e-05, It/sec 0.175, Tokens/sec 167.210, Trained Tokens 655102, Peak mem 33.246 GB
Iter 670: Train loss 0.711, Learning Rate 2.000e-05, It/sec 0.170, Tokens/sec 179.775, Trained Tokens 665703, Peak mem 33.246 GB
Iter 680: Train loss 0.587, Learning Rate 2.000e-05, It/sec 0.159, Tokens/sec 172.598, Trained Tokens 676570, Peak mem 33.246 GB
Iter 690: Train loss 0.567, Learning Rate 2.000e-05, It/sec 0.196, Tokens/sec 179.394, Trained Tokens 685711, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.47s/it]
Iter 700: Val loss 0.644, Val took 347.498s
Iter 700: Train loss 0.666, Learning Rate 2.000e-05, It/sec 0.210, Tokens/sec 189.928, Trained Tokens 694758, Peak mem 33.246 GB
Iter 700: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000700_adapters.safetensors.
Iter 710: Train loss 0.619, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 193.409, Trained Tokens 704733, Peak mem 33.246 GB
Iter 720: Train loss 0.616, Learning Rate 2.000e-05, It/sec 0.171, Tokens/sec 163.031, Trained Tokens 714271, Peak mem 33.246 GB
Iter 730: Train loss 0.630, Learning Rate 2.000e-05, It/sec 0.155, Tokens/sec 154.896, Trained Tokens 724296, Peak mem 33.246 GB
Iter 740: Train loss 0.539, Learning Rate 2.000e-05, It/sec 0.187, Tokens/sec 196.622, Trained Tokens 734783, Peak mem 33.246 GB
Iter 750: Train loss 0.698, Learning Rate 2.000e-05, It/sec 0.186, Tokens/sec 193.588, Trained Tokens 745191, Peak mem 33.246 GB
Iter 760: Train loss 0.643, Learning Rate 2.000e-05, It/sec 0.198, Tokens/sec 188.570, Trained Tokens 754729, Peak mem 33.246 GB
Iter 770: Train loss 0.632, Learning Rate 2.000e-05, It/sec 0.184, Tokens/sec 176.479, Trained Tokens 764320, Peak mem 33.246 GB
Iter 780: Train loss 0.578, Learning Rate 2.000e-05, It/sec 0.183, Tokens/sec 187.576, Trained Tokens 774579, Peak mem 33.246 GB
Iter 790: Train loss 0.697, Learning Rate 2.000e-05, It/sec 0.161, Tokens/sec 161.929, Trained Tokens 784629, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.47s/it]
Iter 800: Val loss 0.639, Val took 347.495s
Iter 800: Train loss 0.588, Learning Rate 2.000e-05, It/sec 0.171, Tokens/sec 169.360, Trained Tokens 794540, Peak mem 33.246 GB
Iter 800: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000800_adapters.safetensors.
Iter 810: Train loss 0.600, Learning Rate 2.000e-05, It/sec 0.210, Tokens/sec 194.657, Trained Tokens 803830, Peak mem 33.246 GB
Iter 820: Train loss 0.575, Learning Rate 2.000e-05, It/sec 0.133, Tokens/sec 151.335, Trained Tokens 815172, Peak mem 33.246 GB
Iter 830: Train loss 0.594, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 185.084, Trained Tokens 825387, Peak mem 33.246 GB
Iter 840: Train loss 0.553, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 185.968, Trained Tokens 835683, Peak mem 33.246 GB
Iter 850: Train loss 0.588, Learning Rate 2.000e-05, It/sec 0.192, Tokens/sec 192.098, Trained Tokens 845680, Peak mem 33.246 GB
Iter 860: Train loss 0.528, Learning Rate 2.000e-05, It/sec 0.192, Tokens/sec 188.010, Trained Tokens 855452, Peak mem 33.246 GB
Iter 870: Train loss 0.479, Learning Rate 2.000e-05, It/sec 0.208, Tokens/sec 195.172, Trained Tokens 864853, Peak mem 33.246 GB
Iter 880: Train loss 0.642, Learning Rate 2.000e-05, It/sec 0.178, Tokens/sec 180.478, Trained Tokens 874971, Peak mem 33.246 GB
Iter 890: Train loss 0.550, Learning Rate 2.000e-05, It/sec 0.214, Tokens/sec 190.068, Trained Tokens 883860, Peak mem 33.246 GB
Calculating loss...: 100it [05:48,  3.48s/it]
Iter 900: Val loss 0.634, Val took 348.016s
Iter 900: Train loss 0.630, Learning Rate 2.000e-05, It/sec 0.150, Tokens/sec 151.560, Trained Tokens 893970, Peak mem 33.246 GB
Iter 900: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0000900_adapters.safetensors.
Iter 910: Train loss 0.612, Learning Rate 2.000e-05, It/sec 0.203, Tokens/sec 194.259, Trained Tokens 903526, Peak mem 33.246 GB
Iter 920: Train loss 0.601, Learning Rate 2.000e-05, It/sec 0.155, Tokens/sec 174.661, Trained Tokens 914784, Peak mem 33.246 GB
Iter 930: Train loss 0.551, Learning Rate 2.000e-05, It/sec 0.198, Tokens/sec 185.549, Trained Tokens 924171, Peak mem 33.246 GB
Iter 940: Train loss 0.498, Learning Rate 2.000e-05, It/sec 0.192, Tokens/sec 189.070, Trained Tokens 933993, Peak mem 33.246 GB
Iter 950: Train loss 0.611, Learning Rate 2.000e-05, It/sec 0.170, Tokens/sec 179.426, Trained Tokens 944517, Peak mem 33.246 GB
Iter 960: Train loss 0.516, Learning Rate 2.000e-05, It/sec 0.209, Tokens/sec 207.369, Trained Tokens 954422, Peak mem 33.246 GB
Iter 970: Train loss 0.540, Learning Rate 2.000e-05, It/sec 0.206, Tokens/sec 192.433, Trained Tokens 963786, Peak mem 33.246 GB
Iter 980: Train loss 0.581, Learning Rate 2.000e-05, It/sec 0.169, Tokens/sec 160.184, Trained Tokens 973257, Peak mem 33.246 GB
Iter 990: Train loss 0.469, Learning Rate 2.000e-05, It/sec 0.218, Tokens/sec 207.059, Trained Tokens 982763, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 1000: Val loss 0.633, Val took 347.817s
Iter 1000: Train loss 0.516, Learning Rate 2.000e-05, It/sec 0.214, Tokens/sec 194.308, Trained Tokens 991860, Peak mem 33.246 GB
Iter 1000: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0001000_adapters.safetensors.
Iter 1010: Train loss 0.621, Learning Rate 2.000e-05, It/sec 0.225, Tokens/sec 192.387, Trained Tokens 1000417, Peak mem 33.246 GB
Iter 1020: Train loss 0.663, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 186.833, Trained Tokens 1010032, Peak mem 33.246 GB
Iter 1030: Train loss 0.557, Learning Rate 2.000e-05, It/sec 0.194, Tokens/sec 182.267, Trained Tokens 1019414, Peak mem 33.246 GB
Iter 1040: Train loss 0.604, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 205.359, Trained Tokens 1030277, Peak mem 33.246 GB
Iter 1050: Train loss 0.564, Learning Rate 2.000e-05, It/sec 0.197, Tokens/sec 187.265, Trained Tokens 1039763, Peak mem 33.246 GB
Iter 1060: Train loss 0.505, Learning Rate 2.000e-05, It/sec 0.140, Tokens/sec 159.153, Trained Tokens 1051119, Peak mem 33.246 GB
Iter 1070: Train loss 0.697, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 178.182, Trained Tokens 1060962, Peak mem 33.246 GB
Iter 1080: Train loss 0.546, Learning Rate 2.000e-05, It/sec 0.205, Tokens/sec 204.018, Trained Tokens 1070896, Peak mem 33.246 GB
Iter 1090: Train loss 0.639, Learning Rate 2.000e-05, It/sec 0.203, Tokens/sec 195.074, Trained Tokens 1080493, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 1100: Val loss 0.628, Val took 347.922s
Iter 1100: Train loss 0.616, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 181.828, Trained Tokens 1090110, Peak mem 33.246 GB
Iter 1100: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0001100_adapters.safetensors.
Iter 1110: Train loss 0.501, Learning Rate 2.000e-05, It/sec 0.214, Tokens/sec 198.885, Trained Tokens 1099421, Peak mem 33.246 GB
Iter 1120: Train loss 0.607, Learning Rate 2.000e-05, It/sec 0.218, Tokens/sec 196.113, Trained Tokens 1108423, Peak mem 33.246 GB
Iter 1130: Train loss 0.580, Learning Rate 2.000e-05, It/sec 0.202, Tokens/sec 193.623, Trained Tokens 1118014, Peak mem 33.246 GB
Iter 1140: Train loss 0.495, Learning Rate 2.000e-05, It/sec 0.165, Tokens/sec 170.989, Trained Tokens 1128364, Peak mem 33.246 GB
Iter 1150: Train loss 0.662, Learning Rate 2.000e-05, It/sec 0.171, Tokens/sec 181.959, Trained Tokens 1139025, Peak mem 33.246 GB
Iter 1160: Train loss 0.596, Learning Rate 2.000e-05, It/sec 0.152, Tokens/sec 166.100, Trained Tokens 1149922, Peak mem 33.246 GB
Iter 1170: Train loss 0.521, Learning Rate 2.000e-05, It/sec 0.137, Tokens/sec 158.334, Trained Tokens 1161503, Peak mem 33.246 GB
Iter 1180: Train loss 0.532, Learning Rate 2.000e-05, It/sec 0.155, Tokens/sec 150.405, Trained Tokens 1171223, Peak mem 33.246 GB
Iter 1190: Train loss 0.566, Learning Rate 2.000e-05, It/sec 0.191, Tokens/sec 186.411, Trained Tokens 1180969, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 1200: Val loss 0.625, Val took 347.978s
Iter 1200: Train loss 0.565, Learning Rate 2.000e-05, It/sec 0.169, Tokens/sec 180.838, Trained Tokens 1191651, Peak mem 33.246 GB
Iter 1200: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0001200_adapters.safetensors.
Iter 1210: Train loss 0.533, Learning Rate 2.000e-05, It/sec 0.170, Tokens/sec 174.119, Trained Tokens 1201912, Peak mem 33.246 GB
Iter 1220: Train loss 0.561, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 196.140, Trained Tokens 1212278, Peak mem 33.246 GB
Iter 1230: Train loss 0.533, Learning Rate 2.000e-05, It/sec 0.212, Tokens/sec 197.880, Trained Tokens 1221629, Peak mem 33.246 GB
Iter 1240: Train loss 0.547, Learning Rate 2.000e-05, It/sec 0.191, Tokens/sec 189.954, Trained Tokens 1231580, Peak mem 33.246 GB
Iter 1250: Train loss 0.644, Learning Rate 2.000e-05, It/sec 0.168, Tokens/sec 172.824, Trained Tokens 1241884, Peak mem 33.246 GB
Iter 1260: Train loss 0.503, Learning Rate 2.000e-05, It/sec 0.179, Tokens/sec 188.885, Trained Tokens 1252421, Peak mem 33.246 GB
Iter 1270: Train loss 0.646, Learning Rate 2.000e-05, It/sec 0.227, Tokens/sec 198.888, Trained Tokens 1261177, Peak mem 33.246 GB
Iter 1280: Train loss 0.549, Learning Rate 2.000e-05, It/sec 0.205, Tokens/sec 189.953, Trained Tokens 1270421, Peak mem 33.246 GB
Iter 1290: Train loss 0.540, Learning Rate 2.000e-05, It/sec 0.182, Tokens/sec 184.564, Trained Tokens 1280536, Peak mem 33.246 GB
Calculating loss...: 100it [05:47,  3.48s/it]
Iter 1300: Val loss 0.620, Val took 347.837s
Iter 1300: Train loss 0.564, Learning Rate 2.000e-05, It/sec 0.178, Tokens/sec 181.426, Trained Tokens 1290755, Peak mem 33.246 GB
Iter 1300: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0001300_adapters.safetensors.
Iter 1310: Train loss 0.708, Learning Rate 2.000e-05, It/sec 0.160, Tokens/sec 169.463, Trained Tokens 1301345, Peak mem 33.246 GB
Iter 1320: Train loss 0.598, Learning Rate 2.000e-05, It/sec 0.203, Tokens/sec 185.313, Trained Tokens 1310454, Peak mem 33.246 GB
Iter 1330: Train loss 0.583, Learning Rate 2.000e-05, It/sec 0.200, Tokens/sec 195.836, Trained Tokens 1320247, Peak mem 33.246 GB
Iter 1340: Train loss 0.657, Learning Rate 2.000e-05, It/sec 0.169, Tokens/sec 187.666, Trained Tokens 1331340, Peak mem 33.246 GB
Iter 1350: Train loss 0.565, Learning Rate 2.000e-05, It/sec 0.156, Tokens/sec 183.221, Trained Tokens 1343062, Peak mem 33.246 GB
Iter 1360: Train loss 0.508, Learning Rate 2.000e-05, It/sec 0.202, Tokens/sec 202.290, Trained Tokens 1353093, Peak mem 33.246 GB
Iter 1370: Train loss 0.542, Learning Rate 2.000e-05, It/sec 0.224, Tokens/sec 191.831, Trained Tokens 1361647, Peak mem 33.246 GB
Iter 1380: Train loss 0.512, Learning Rate 2.000e-05, It/sec 0.179, Tokens/sec 178.724, Trained Tokens 1371607, Peak mem 33.246 GB
Iter 1390: Train loss 0.593, Learning Rate 2.000e-05, It/sec 0.175, Tokens/sec 185.533, Trained Tokens 1382226, Peak mem 33.246 GB
Calculating loss...: 100it [05:48,  3.48s/it]
Iter 1400: Val loss 0.617, Val took 348.005s
Iter 1400: Train loss 0.600, Learning Rate 2.000e-05, It/sec 0.181, Tokens/sec 189.143, Trained Tokens 1392688, Peak mem 33.246 GB
Iter 1400: Saved adapter weights to adapters/05/adapters.safetensors and adapters/05/0001400_adapters.safetensors.
Iter 1410: Train loss 0.581, Learning Rate 2.000e-05, It/sec 0.177, Tokens/sec 180.385, Trained Tokens 1402899, Peak mem 33.246 GB
Iter 1420: Train loss 0.540, Learning Rate 2.000e-05, It/sec 0.204, Tokens/sec 189.951, Trained Tokens 1412219, Peak mem 33.246 GB
Iter 1430: Train loss 0.519, Learning Rate 2.000e-05, It/sec 0.148, Tokens/sec 154.158, Trained Tokens 1422665, Peak mem 33.246 GB
Iter 1440: Train loss 0.618, Learning Rate 2.000e-05, It/sec 0.212, Tokens/sec 193.938, Trained Tokens 1431826, Peak mem 33.246 GB
Iter 1450: Train loss 0.585, Learning Rate 2.000e-05, It/sec 0.208, Tokens/sec 184.873, Trained Tokens 1440722, Peak mem 33.246 GB
Iter 1460: Train loss 0.589, Learning Rate 2.000e-05, It/sec 0.193, Tokens/sec 181.133, Trained Tokens 1450109, Peak mem 33.246 GB
Iter 1470: Train loss 0.581, Learning Rate 2.000e-05, It/sec 0.189, Tokens/sec 173.812, Trained Tokens 1459287, Peak mem 33.246 GB
Iter 1480: Train loss 0.570, Learning Rate 2.000e-05, It/sec 0.214, Tokens/sec 196.334, Trained Tokens 1468451, Peak mem 33.246 GB
Iter 1490: Train loss 0.670, Learning Rate 2.000e-05, It/sec 0.196, Tokens/sec 188.836, Trained Tokens 1478063, Peak mem 33.246 GB

```