1. pull Msitral-7B-v0.3 from Hugging Face and convert to 4-bit quantized
```
mlx_lm.convert \
    --hf-path mistralai/Mistral-7B-Instruct-v0.3 \
    -q 
```

Qwen/Qwen2.5-1.5B-Instruct

2. fine-tune quantized model with data
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
   --adapter-path ./adapters/adapters-mistral \
   --save-every 50 \
   --test \
   --test-batches -1 
```

3. Fuse original model with adapters
```
mlx_lm.fuse \
--model mistralai/Mistral-7B-v0.3 \
--adapter-path ./adapters/adapters-mistral \
--save-path ./mistral-7b-v0.3-fused
```

4. convert model to gguf (switch to llama.cpp repo)
```
python convert_hf_to_gguf.py /Users/rtueremis/Metaeffekt/Repositories/thm-bachelorthesis/fine-tuning/mistral-7b-v0.3-fused --outfile mistral-7b-fused-f16.gguf --outtype f16
```

5. quantize model
```
./build/bin/llama-quantize \
  mistral-7b-fused-f16.gguf \  
  mistral-7b-fused.Q4_K_M.gguf \
  Q4_K_M
```


6. create Modelfile
```
nano Modelfile
```

```
FROM ./mistral-7b-fused.Q4_K_M.gguf

PARAMETER num_ctx 4096
PARAMETER temperature 0.0
SYSTEM Extract the copyrights, holders and authors in JSON format from the following file:
```

7. add to ollama
```
ollama create mistral-7b-finetuned-01 -f Modelfile
```
