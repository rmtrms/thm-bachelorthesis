# Benchmark Sprachmodelle

## Tasks

- [x] Umgebung einrichten
- [x] Modelle auswählen
- [x] Prompt Engineering
- [x] Daten generieren 
- [x] Metriken bestimmen
- [x] Daten auswerten

## Modelle für Benchmark

- llama4
- llama3:8b
- qwen3:8b(30b)
- deepseek-r1:8b
- gemma3:4b
- phi4:14b
- mistral:7b

## Probleme

- Die Größe des Basis-Prompt zusammen mit dem Inhalt der Input-Datei sprengt den Kontext. Die Modelle generieren ab diesem Punkt nur noch schlechte Outputs. Um dies zu verhindern wird versucht, mit Hilfe eines TextSieve die Eingangsdatei auf Copyright relevante Inhalte zu kürzen und Codezeilen weitestgehend zu entfernen.
- Kleinere Modelle (phi2) scheinen die Aufgabe falsch zu verstehen. Statt dem erwarteten Output wird Code generiert, der die Aufgabe lösen soll, scheinbar verursachen die vorhandenen Code-Segmente diese falsche Einschätzung durch das Sprachmodell
- Output JSON wird auf verschiedene Weisen in z.B. Markdown gewrapped, oder als Liste von JSON Objekten zurückgegeben usw. --> wurde mit Yans ChatUtil behoben
- Timeout bei der Anfrage --> Single Thread Executor Service nutzen um nach 5 Minuten den Versuch abzubrechen

## Links

- [Prompt Engineering for small llms](https://maliknaik.medium.com/prompt-engineering-for-small-llms-llama-3b-qwen-4b-and-phi-3-mini-de711d38a002)
- [Best practices for prompt engineering with OpenAI API](https://help.openai.com/en/articles/6654000-best-practices-for-prompt-engineering-with-the-openai-api)

## Prompt Engineering tips from AIs

- prompt: how can I improve my prompts for most types of ai models? Suggest a way to prompt for information extraction

---

**Qwen3** (8b, 27.06.2025)

Improving prompts for AI models (especially for tasks like information
extraction) requires **clarity, specificity, and structure**. Here's a
systematic approach to crafting effective prompts, with a focus on
**information extraction**:

### **General Tips for Improving Prompts**
1. **Be Specific About the Task**
    - Clearly define what you want the model to extract (e.g., names,
      dates, relationships, entities, etc.).
    - Example: *"Extract all the names of people mentioned in this text."*

2. **Provide Examples**
    - Include sample input/output pairs to guide the model.
    - Example:  
      **Input**: "John works at Google. He was born in 1990."  
      **Output**:
        - Name: John
        - Company: Google
        - Birth Year: 1990

3. **Specify the Output Format**
    - Use structured formats like bullet points, tables, or JSON to make
      the output predictable.
    - Example: *"List the extracted information in a JSON format with keys
      like 'Name', 'Date', and 'Location'."*

4. **Use Delimiters or Markers**
    - Separate sections (e.g., input, examples, instructions) to help the
      model parse the prompt.
- Example:
```  
  [Input Text]  
  "John works at Google. He was born in 1990."  
 
  [Examples]  
  Input: "Alice joined Microsoft in 2015."  
  Output: {"Name": "Alice", "Company": "Microsoft", "Year": 2015}  
 
  [Instructions]  
  Extract the name, company, and year from the input text. Output in 
JSON format.  
```

5. **Iterate and Refine**
    - Test the prompt with different inputs and refine it based on the
      model's output.

---

### **Prompt Template for Information Extraction**
Here’s a reusable template for extracting structured information:

```  
[Task]  
Extract [specific type of information, e.g., "names, dates, and 
locations"] from the following text.  

[Input Text]  
{Insert the text here}  

[Examples]  
Example 1:  
Input: "Maria was born in 1985 in Paris."  
Output: {"Name": "Maria", "Birth Year": 1985, "Location": "Paris"}  

Example 2:  
Input: "The meeting is scheduled for October 5, 2023, in New York."  
Output: {"Event": "meeting", "Date": "October 5, 2023", "Location": "New 
York"}  

[Instructions]  
1. Identify all instances of [specific information type].  
2. Format the output as [JSON, bullet points, table, etc.].  
3. Avoid adding extra details not present in the input.  
4. Use the exact wording from the input for accuracy.  
```  

---

### **Key Considerations for Different Models**
- **For LLMs (e.g., GPT-4, Claude):**  
  Use natural language instructions and examples.
- **For specialized models (e.g., NLP pipelines):**  
  Use precise labels (e.g., "NER tags" for named entity recognition).
- **For code-based models (e.g., Codex):**  
  Include code snippets or schema definitions.

---

### **Example Prompt for Information Extraction**
```  
[Task]  
Extract all company names, dates, and locations mentioned in the following 
text.  

[Input Text]  
"Samsung announced a new product launch on March 15, 2024, in Seoul. The 
event will be held at the Samsung Innovation Center."  

[Examples]  
Input: "Apple released the iPhone 15 on September 12, 2023, in Cupertino." 
 
Output: {"Company": "Apple", "Date": "September 12, 2023", "Location": 
"Cupertino"}  

[Instructions]  
- Extract company names, dates, and locations.  
- Output in JSON format with keys: "Company", "Date", "Location".  
- Use exact dates and locations from the text.  
```  

By structuring your prompts this way, you increase the likelihood of
accurate and consistent information extraction across most AI models.

---

**Llama4** (16b, 27.06.2025)

Improving prompts for AI models, including those for information
extraction, involves clarity, specificity, and understanding the model's
capabilities. Here are some general tips and a specific approach for
information extraction:

### General Tips for Effective Prompts

1. **Be Specific**: Clearly define what you are asking for. Specificity
   helps the model understand the context and provide a more accurate
   response.

2. **Use Simple Language**: Avoid jargon unless necessary for the context.
   Simple language makes it easier for the model to understand the prompt.

3. **Define the Scope**: Indicate the scope of the information you're
   seeking. This could be a specific time frame, geographic location, or any
   other delimiters relevant to your query.

4. **Indicate the Format**: If you need the information in a specific
   format (e.g., list, summary, detailed report), let the model know.

5. **Provide Examples**: When possible, provide examples of the type of
   information you're looking for. This can help the model better understand
   your needs.

6. **Avoid Ambiguity**: Try to phrase your prompts in a way that leaves
   little room for interpretation.

### Prompting for Information Extraction

Information extraction involves pulling specific details from text. Here’s
how to craft prompts for this task:

1. **Specify the Task**: Clearly state that you want to extract
   information. For example, "Extract the names, dates, and locations from
   the following text."

2. **Define the Information Type**: Indicate what type of information you
   want to extract. For instance, if you're extracting from a biographical
   text, you might ask for "key achievements, birth date, and nationality."

3. **Indicate the Output Format**: Specify how you want the extracted
   information formatted. For example, "Provide a list of names and
   corresponding dates."

4. **Provide Context**: Give some context about why you're extracting this
   information or what you plan to do with it. This can help in getting more
   relevant results.

### Example Prompt for Information Extraction

**Prompt**: "From the following text, extract and list the names of
people, organizations, and locations. Provide the list in a table format
with clear headers.

**Text**: [Insert your text here]."

### Advanced Prompting Techniques

- **Zero-shot, Few-shot, and Chain-of-thought Prompting**: Explore these
  advanced techniques for more complex tasks. Zero-shot learning involves
  asking the model to perform a task without examples. Few-shot learning
  provides a few examples to guide the model. Chain-of-thought prompting
  involves asking the model to show its reasoning step by step.

- **Iterative Refinement**: Don't be afraid to refine your prompts based
  on the model's responses. This can help in getting more accurate results.

By following these guidelines, you can improve the effectiveness of your
prompts for information extraction and other tasks, making it easier to
get the information you need from AI models.

---

**deepseek-r1** (8b, 27.06.2025)

Great question! Improving your prompts for AI models is all about
clarity, specificity, and understanding the task. Here's a general
approach to enhance prompts across most types of AI models:

### **1. Define Your Goal Clearly**
Start by stating exactly what you want the AI to do in one or two
sentences.

**Example:**  
_"Extract the names of people mentioned in this text along with
their roles, and list them as bullet points."_

### **2. Provide Context**
If necessary, give background information about the task or explain
the output format.

**Example:**  
_"You are an expert data analyst. Extract specific details from a
news article and return them in JSON format (e.g., { 'title': '',
'date': '', 'key_actors': [] })."_

### **3. Break Down Instructions**
Divide complex tasks into smaller steps if possible.

**Example:**  
_"First, summarize the main idea of this text.\nThen, extract three
key points in a numbered list."_

### **4. Use Examples (Few-Shot Prompting)**
Give examples to illustrate what you expect from the output.

**Example:**  
_"Text: 'The CEO announced new policies.'\nExtracted information:
{'CEO': 'person', 'role': 'chief executive officer'}\n\nNow, process
this text: 'The scientist discovered a cure for cancer.'"_

### **5. Specify Output Format**
Always set the structure of your desired output.

**Example:**  
_"Please extract all names and their associated roles from this text
and return them in JSON format as an array of objects (e.g.,
[{‘name’: ‘John Doe’, ‘role’: ‘CEO’}, {‘name’: ‘Jane Smith’, ‘role’:
‘CTO’}])"_.

---

### **Prompt for Information Extraction**

Here's a structured prompt you can use to extract specific
information from any text:

```prompt
Extract all the following details from this text and return them in 
JSON format with keys: 'names', 'roles'. Each entry should be an 
object containing:
- name (string)
- role (string)

If there is no relevant information, please state so.

Text: [Your input text here]

Output Example:
{
  "names": [
    { "name": "John Doe", "role": "CEO" },
    { "name": "Jane Smith", "role": "CTO" }
  ]
}

Please only output the JSON structure without any additional 
explanation.
```

This prompt clearly defines what information to extract and how it
should be structured. You can adjust the keys according to your
needs (e.g., 'dates', 'locations', etc.).

---

### **Tips for Better Prompting**
- Use specific terms like "CEO", not vague ones like "important
  person".
- Ask the model to explain any ambiguities.
- Include constraints, such as maximum length or minimum
  specificity.

Let me know what kind of information extraction you'd like help with!