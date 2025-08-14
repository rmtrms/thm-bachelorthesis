 # Steps
 
## Setup

- clone mlx examples
- create virtual environment for python 3.10
- install mlx requirements
```
pip install -r requirements.txt
```

## Finetuning
- convert mistral to 4-bit quantized
```
python convert.py --hf-path mistralai/Mistral-7B-Instruct-v0.3 -q
```
- finetuning with training dataset
```
      python lora.py --model ./mlx_model \
               --train \
               --data ./data \
               --lora-layers 8 \
               --iters 300 \
               --batch-size 4 \
               --val-batches -1 \
               --learning-rate 5e-5 \
               --steps-per-report 10 \
               --steps-per-eval 20 \
               --adapter-file ./adapters/01_adapter.npz \
               --save-every 50 \
               --test \
               --test-batches -1 \
               --temp 0.0 \
               --seed 42 
```

`--val batches -1` --> use entire validation dataset each time
`--lora-layers 8` --> train subset of layers for ressource management
`--learning-rate 5e-5` --> default learning rate
`--temp 0.0` adjust temp during testing for deterministic output 
`--seed 42` ensure reproducibility

### Runs
#### 001
```
python lora.py --model ./mlx_model \
--train \
--data ./data \
--lora-layers 8 \
--iters 300 \
--batch-size 4 \
--val-batches -1 \
--learning-rate 5e-5 \
--steps-per-report 10 \
--steps-per-eval 20 \
--adapter-file ./adapters/01_adapter.npz \
--save-every 50 \
--test \
--test-batches -1 \
--temp 0.0 \
--seed 42
Loading pretrained model
Total parameters 1133.580M
Trainable parameters 0.852M
Loading datasets
Training
Iter 1: Val loss 1.737, Val took 60.725s
Iter 10: Train loss 1.434, It/sec 0.221, Tokens/sec 268.476
Iter 20: Train loss 0.937, It/sec 0.184, Tokens/sec 206.650
Iter 20: Val loss 0.942, Val took 60.434s
Iter 30: Train loss 0.837, It/sec 0.181, Tokens/sec 194.378
Iter 40: Train loss 0.950, It/sec 0.204, Tokens/sec 206.746
Iter 40: Val loss 0.879, Val took 60.245s
Iter 50: Train loss 0.883, It/sec 0.230, Tokens/sec 226.336
Iter 50: Saved adapter weights to ./adapters/01_adapter.npz.
Iter 60: Train loss 0.987, It/sec 0.170, Tokens/sec 187.449
Iter 60: Val loss 0.828, Val took 60.233s
Iter 70: Train loss 0.772, It/sec 0.173, Tokens/sec 185.403
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 80: Train loss 0.793, It/sec 0.118, Tokens/sec 155.624
Iter 80: Val loss 0.791, Val took 60.241s
Iter 90: Train loss 0.865, It/sec 0.199, Tokens/sec 218.428
Iter 100: Train loss 0.810, It/sec 0.196, Tokens/sec 205.267
Iter 100: Val loss 0.779, Val took 60.297s
Iter 100: Saved adapter weights to ./adapters/01_adapter.npz.
Iter 110: Train loss 0.787, It/sec 0.231, Tokens/sec 215.910
Iter 120: Train loss 0.713, It/sec 0.188, Tokens/sec 201.931
Iter 120: Val loss 0.763, Val took 60.363s
Iter 130: Train loss 0.692, It/sec 0.245, Tokens/sec 230.892
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 140: Train loss 0.717, It/sec 0.123, Tokens/sec 156.732
Iter 140: Val loss 0.748, Val took 59.973s
Iter 150: Train loss 0.729, It/sec 0.213, Tokens/sec 238.547
Iter 150: Saved adapter weights to ./adapters/01_adapter.npz.
Iter 160: Train loss 0.580, It/sec 0.217, Tokens/sec 225.396
Iter 160: Val loss 0.741, Val took 60.368s
Iter 170: Train loss 0.583, It/sec 0.211, Tokens/sec 217.636
Iter 180: Train loss 0.682, It/sec 0.188, Tokens/sec 213.720
Iter 180: Val loss 0.731, Val took 60.323s
Iter 190: Train loss 0.634, It/sec 0.160, Tokens/sec 177.965
Iter 200: Train loss 0.789, It/sec 0.139, Tokens/sec 162.777
Iter 200: Val loss 0.724, Val took 60.397s
Iter 200: Saved adapter weights to ./adapters/01_adapter.npz.
Iter 210: Train loss 0.647, It/sec 0.166, Tokens/sec 183.820
Iter 220: Train loss 0.639, It/sec 0.214, Tokens/sec 229.958
Iter 220: Val loss 0.712, Val took 60.490s
Iter 230: Train loss 0.699, It/sec 0.190, Tokens/sec 188.318
Iter 240: Train loss 0.650, It/sec 0.182, Tokens/sec 192.491
Iter 240: Val loss 0.714, Val took 60.388s
Iter 250: Train loss 0.541, It/sec 0.202, Tokens/sec 215.919
Iter 250: Saved adapter weights to ./adapters/01_adapter.npz.
Iter 260: Train loss 0.466, It/sec 0.220, Tokens/sec 237.512
Iter 260: Val loss 0.718, Val took 60.440s
Iter 270: Train loss 0.519, It/sec 0.164, Tokens/sec 185.065
Iter 280: Train loss 0.575, It/sec 0.208, Tokens/sec 217.561
Iter 280: Val loss 0.724, Val took 60.431s
Iter 290: Train loss 0.514, It/sec 0.223, Tokens/sec 219.607
Iter 300: Train loss 0.501, It/sec 0.212, Tokens/sec 216.710
Iter 300: Val loss 0.716, Val took 60.300s
Iter 300: Saved adapter weights to ./adapters/01_adapter.npz.
Testing
Test loss 0.754, Test ppl 2.127.
```
#### 002
```
python lora.py --model ./mlx_model \
               --train \
               --data ./data \
               --lora-layers 8 \
               --iters 300 \
               --batch-size 4 \
               --val-batches -1 \
               --learning-rate 1e-5 \
               --steps-per-report 10 \
               --steps-per-eval 20 \
               --adapter-file ./adapters/02_adapter.npz \
               --save-every 50 \
               --test \
               --test-batches -1 \
               --temp 0.0 \
               --seed 42
Loading pretrained model
Total parameters 1133.580M
Trainable parameters 0.852M
Loading datasets
Training
Iter 1: Val loss 1.849, Val took 58.872s
Iter 10: Train loss 1.736, It/sec 0.224, Tokens/sec 272.492
Iter 20: Train loss 1.270, It/sec 0.187, Tokens/sec 209.333
Iter 20: Val loss 1.193, Val took 60.065s
Iter 30: Train loss 1.047, It/sec 0.182, Tokens/sec 195.505
Iter 40: Train loss 1.100, It/sec 0.205, Tokens/sec 207.906
Iter 40: Val loss 1.011, Val took 60.161s
Iter 50: Train loss 1.008, It/sec 0.230, Tokens/sec 226.253
Iter 50: Saved adapter weights to ./adapters/02_adapter.npz.
Iter 60: Train loss 1.083, It/sec 0.170, Tokens/sec 187.362
Iter 60: Val loss 0.935, Val took 60.322s
Iter 70: Train loss 0.869, It/sec 0.172, Tokens/sec 184.836
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 80: Train loss 0.909, It/sec 0.119, Tokens/sec 157.449
Iter 80: Val loss 0.897, Val took 60.481s
Iter 90: Train loss 0.955, It/sec 0.198, Tokens/sec 217.344
Iter 100: Train loss 0.873, It/sec 0.195, Tokens/sec 204.265
Iter 100: Val loss 0.875, Val took 60.602s
Iter 100: Saved adapter weights to ./adapters/02_adapter.npz.
Iter 110: Train loss 0.875, It/sec 0.229, Tokens/sec 214.385
Iter 120: Train loss 0.817, It/sec 0.187, Tokens/sec 200.777
Iter 120: Val loss 0.859, Val took 60.677s
Iter 130: Train loss 0.843, It/sec 0.244, Tokens/sec 229.619
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 140: Train loss 0.847, It/sec 0.122, Tokens/sec 154.951
Iter 140: Val loss 0.844, Val took 60.128s
Iter 150: Train loss 0.879, It/sec 0.212, Tokens/sec 237.657
Iter 150: Saved adapter weights to ./adapters/02_adapter.npz.
Iter 160: Train loss 0.715, It/sec 0.216, Tokens/sec 224.529
Iter 160: Val loss 0.834, Val took 60.705s
Iter 170: Train loss 0.712, It/sec 0.210, Tokens/sec 216.231
Iter 180: Train loss 0.835, It/sec 0.187, Tokens/sec 212.450
Iter 180: Val loss 0.826, Val took 60.742s
Iter 190: Train loss 0.774, It/sec 0.158, Tokens/sec 176.355
Iter 200: Train loss 0.932, It/sec 0.138, Tokens/sec 161.364
Iter 200: Val loss 0.820, Val took 60.777s
Iter 200: Saved adapter weights to ./adapters/02_adapter.npz.
Iter 210: Train loss 0.782, It/sec 0.165, Tokens/sec 182.717
Iter 220: Train loss 0.788, It/sec 0.212, Tokens/sec 228.256
Iter 220: Val loss 0.814, Val took 60.779s
Iter 230: Train loss 0.863, It/sec 0.189, Tokens/sec 187.011
Iter 240: Train loss 0.823, It/sec 0.181, Tokens/sec 191.203
Iter 240: Val loss 0.809, Val took 60.770s
Iter 250: Train loss 0.731, It/sec 0.201, Tokens/sec 214.307
Iter 250: Saved adapter weights to ./adapters/02_adapter.npz.
Iter 260: Train loss 0.654, It/sec 0.218, Tokens/sec 236.060
Iter 260: Val loss 0.802, Val took 60.762s
Iter 270: Train loss 0.708, It/sec 0.163, Tokens/sec 183.713
Iter 280: Train loss 0.801, It/sec 0.206, Tokens/sec 216.055
Iter 280: Val loss 0.798, Val took 60.808s
Iter 290: Train loss 0.716, It/sec 0.221, Tokens/sec 218.098
Iter 300: Train loss 0.704, It/sec 0.211, Tokens/sec 215.824
Iter 300: Val loss 0.792, Val took 60.716s
Iter 300: Saved adapter weights to ./adapters/02_adapter.npz.
Testing
Test loss 0.832, Test ppl 2.298.
```

#### 003

```
python lora.py --model ./mlx_model \
               --train \
               --data ./data \
               --lora-layers 16 \
               --iters 300 \
               --batch-size 4 \
               --val-batches -1 \
               --learning-rate 5e-5 \
               --steps-per-report 10 \
               --steps-per-eval 20 \
               --adapter-file ./adapters/03_adapter.npz \
               --save-every 50 \
               --test \
               --test-batches -1 \
               --temp 0.0 \
               --seed 42
Loading pretrained model
Total parameters 1134.432M
Trainable parameters 1.704M
Loading datasets
Training
Iter 1: Val loss 1.586, Val took 62.491s
Iter 10: Train loss 1.312, It/sec 0.176, Tokens/sec 213.622
Iter 20: Train loss 0.876, It/sec 0.148, Tokens/sec 165.383
Iter 20: Val loss 0.916, Val took 63.205s
Iter 30: Train loss 0.805, It/sec 0.143, Tokens/sec 153.441
Iter 40: Train loss 0.920, It/sec 0.161, Tokens/sec 163.278
Iter 40: Val loss 0.841, Val took 63.483s
Iter 50: Train loss 0.858, It/sec 0.180, Tokens/sec 177.333
Iter 50: Saved adapter weights to ./adapters/03_adapter.npz.
Iter 60: Train loss 0.945, It/sec 0.133, Tokens/sec 146.891
Iter 60: Val loss 0.795, Val took 63.711s
Iter 70: Train loss 0.740, It/sec 0.135, Tokens/sec 145.161
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 80: Train loss 0.771, It/sec 0.045, Tokens/sec 59.413
Iter 80: Val loss 0.764, Val took 67.326s
Iter 90: Train loss 0.841, It/sec 0.152, Tokens/sec 167.678
Iter 100: Train loss 0.785, It/sec 0.153, Tokens/sec 160.292
Iter 100: Val loss 0.745, Val took 63.524s
Iter 100: Saved adapter weights to ./adapters/03_adapter.npz.
Iter 110: Train loss 0.749, It/sec 0.182, Tokens/sec 169.852
Iter 120: Train loss 0.666, It/sec 0.148, Tokens/sec 158.885
Iter 120: Val loss 0.724, Val took 63.470s
Iter 130: Train loss 0.625, It/sec 0.193, Tokens/sec 181.883
[WARNING] Some sequences are longer than 2048 tokens. Consider pre-splitting your data to save memory.
Iter 140: Train loss 0.762, It/sec 0.047, Tokens/sec 60.307
Iter 140: Val loss 0.825, Val took 65.060s
Iter 150: Train loss 0.722, It/sec 0.161, Tokens/sec 180.335
Iter 150: Saved adapter weights to ./adapters/03_adapter.npz.
Iter 160: Train loss 0.579, It/sec 0.168, Tokens/sec 173.848
Iter 160: Val loss 0.742, Val took 63.954s
Iter 170: Train loss 0.558, It/sec 0.165, Tokens/sec 170.609
Iter 180: Train loss 0.645, It/sec 0.148, Tokens/sec 167.622
Iter 180: Val loss 0.713, Val took 63.661s
Iter 190: Train loss 0.594, It/sec 0.125, Tokens/sec 139.717
Iter 200: Train loss 0.748, It/sec 0.109, Tokens/sec 127.735
Iter 200: Val loss 0.701, Val took 63.674s
Iter 200: Saved adapter weights to ./adapters/03_adapter.npz.
Iter 210: Train loss 0.607, It/sec 0.130, Tokens/sec 144.440
Iter 220: Train loss 0.599, It/sec 0.168, Tokens/sec 180.509
Iter 220: Val loss 0.686, Val took 63.726s
Iter 230: Train loss 0.639, It/sec 0.150, Tokens/sec 148.054
Iter 240: Train loss 0.613, It/sec 0.143, Tokens/sec 151.259
Iter 240: Val loss 0.685, Val took 63.625s
Iter 250: Train loss 0.481, It/sec 0.159, Tokens/sec 169.408
Iter 250: Saved adapter weights to ./adapters/03_adapter.npz.
Iter 260: Train loss 0.414, It/sec 0.172, Tokens/sec 186.419
Iter 260: Val loss 0.697, Val took 63.769s
Iter 270: Train loss 0.466, It/sec 0.129, Tokens/sec 144.932
Iter 280: Train loss 0.499, It/sec 0.163, Tokens/sec 170.682
Iter 280: Val loss 0.704, Val took 63.768s
Iter 290: Train loss 0.442, It/sec 0.175, Tokens/sec 172.249
Iter 300: Train loss 0.439, It/sec 0.167, Tokens/sec 170.685
Iter 300: Val loss 0.692, Val took 63.701s
Iter 300: Saved adapter weights to ./adapters/03_adapter.npz.
Testing
Test loss 0.729, Test ppl 2.073.
```

## Fuse Adapters with model
```
python fuse.py \
  --model ./mlx_model \
  --save-path ./fused/01 \
  --adapter-file ./adapters/01_adapter.npz \
  --de-quantize
```

## Convert to GGUF

```
python /Users/rtueremis/Metaeffekt/Repositories/llama.cpp/convert_hf_to_gguf.py \
  /Users/rtueremis/Metaeffekt/Repositories/mlx-examples/lora/fused_hf/01 \
  --outfile /Users/rtueremis/Metaeffekt/Repositories/mlx-examples/lora/gguf_model/01.gguf \
  --outtype f16
```

```
INFO:hf-to-gguf:Loading model: 01
INFO:hf-to-gguf:Model architecture: MistralForCausalLM
INFO:gguf.gguf_writer:gguf: This GGUF file is for Little Endian only
INFO:hf-to-gguf:Exporting model...
INFO:hf-to-gguf:gguf: loading model weight map from 'model.safetensors.index.json'
INFO:hf-to-gguf:gguf: loading model part 'model.safetensors'
Traceback (most recent call last):
  File "/llama.cpp/convert_hf_to_gguf.py", line 8595, in <module>
    main()
  File "/llama.cpp/convert_hf_to_gguf.py", line 8589, in main
    model_instance.write()
  File "/llama.cpp/convert_hf_to_gguf.py", line 410, in write
    self.prepare_tensors()
  File "/llama.cpp/convert_hf_to_gguf.py", line 2126, in prepare_tensors
    super().prepare_tensors()
  File "/llama.cpp/convert_hf_to_gguf.py", line 277, in prepare_tensors
    for new_name, data_torch in (self.modify_tensors(data_torch, name, bid)):
  File "/llama.cpp/convert_hf_to_gguf.py", line 2093, in modify_tensors
    return [(self.map_tensor_name(name), data_torch)]
  File "/llama.cpp/convert_hf_to_gguf.py", line 236, in map_tensor_name
    raise ValueError(f"Can not map tensor {name!r}")
ValueError: Can not map tensor 'lm_head.scales'
```