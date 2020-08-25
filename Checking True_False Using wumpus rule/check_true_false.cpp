#include <stdio.h>
#include <ctype.h>
#include <cstring>
#include <cstdlib>
#define string_length strlen

#include "check_true_false.h"



logical_expression::logical_expression()
{
  symbol[0] = 0;
  connective[0] = 0;
}


logical_expression::~logical_expression()
{
  unsigned long counter;
  for (counter = 0; counter < subexpressions.size(); counter++)
  {
    delete subexpressions[counter];
  }
}


void print_expression(logical_expression * expression, char * separator)
{
  if (expression == 0)
  {
    printf("\nINVALID\n");
  }
  else if (strcmp(expression->symbol, "") != 0)
  {
    printf("%s", expression->symbol);
  }
  else
  {
    printf("(%s",  expression->connective);
    unsigned long counter;
    for (counter = 0; counter < expression->subexpressions.size(); counter++)
    {
      printf(" ");
      print_expression(expression->subexpressions[counter], "");
      printf(separator);
    }
    printf(")");
  }
}


logical_expression * read_expression(char * input_string)
{
  long counter = 0;
  return read_expression(input_string, counter);
}


logical_expression * read_expression(char * input_string, long & counter)
{
  logical_expression * result = new logical_expression();
  long length =  string_length(input_string);
  while(1)
  {
    if (counter >= length)
    {
      break;
    }
    if (isspace(input_string[counter]))    // skip whitespace
    {
      counter++;
      continue;
    }
    else if (input_string[counter] == '(')
    {
      counter++;
      read_word(input_string, counter, result->connective);
      read_subexpressions(input_string, counter, result->subexpressions);
      break;
    }
    else
    {
      read_word(input_string, counter, result->symbol);
      break;
    }
  }

  return result;
}


long read_subexpressions(char * input_string, long & counter,
                         vector <logical_expression*> & subexpressions)
{
  long length =  string_length(input_string);
  while(1)
  {
    if (counter >= length)
    {
      printf("\nunexpected end of input\n");
      return 0;
    }
    if (isspace(input_string[counter]))    // skip whitespace
    {
      counter++;
      continue;
    }
    if (input_string[counter] == ')') // we are done
    {
      counter++;
      return 1;
    }
    else
    {
      logical_expression * expression = read_expression(input_string, counter);
      subexpressions.push_back(expression);
    }
  }
}


void read_word(char * input_string, long & counter, char * target)
{
  unsigned long second_counter = 0;
  while (1)
  {
    if (counter >= (long) string_length(input_string))
    {
      break;
    }
    if ((isalpha(input_string[counter])) || (input_string[counter] == '_') ||
        (isdigit(input_string[counter])))
    {
      target[second_counter] = input_string[counter];
      counter++;
      second_counter++;
    }
    else if ((input_string[counter] == ')') || (isspace(input_string[counter])))
    {
      break;
    }
    else
    {
      printf("unexpected character %c\n", input_string[counter]);
      exit_function(0);
    }
  }

  target[second_counter] = 0;
}


long valid_expression(logical_expression *  expression)
{
  if (strcmp(expression->symbol, "") != 0)
  {
    return valid_symbol(expression->symbol);
  }

  if ((strcasecmp(expression->connective, "if") == 0) ||
      (strcasecmp(expression->connective, "iff") == 0))
  {
    if (expression->subexpressions.size() != 2)
    {
      printf("error: connective \"%s\" with %li arguments\n",
             expression->connective, expression->subexpressions.size());
      return 0;
      return 0;
    }
  }
  else   if (strcasecmp(expression->connective, "not") == 0)
  {
    if (expression->subexpressions.size() != 1)
    {
      printf("error: connective \"%s\" with %li arguments\n",
             expression->connective, expression->subexpressions.size());
      return 0;
    }
  }
  else if ((strcasecmp(expression->connective, "and") != 0) &&
           (strcasecmp(expression->connective, "or") != 0) &&
           (strcasecmp(expression->connective, "xor") != 0))
  {
    printf("error: unknown connective %s\n", expression->connective);
    return 0;
  }

  unsigned long counter;
  for (counter = 0; counter < expression->subexpressions.size(); counter++)
  {
    if (valid_expression(expression->subexpressions[counter]) == 0)
    {
      return 0;
    }
  }

  return 1;
}


long valid_symbol(char * symbol)
{
  if ((symbol == 0) || (strlen(symbol) == 0))
  {
    return 0;
  }
  unsigned long counter;
  for (counter = 0; counter < strlen(symbol);  counter++)
  {
    if ((symbol[counter] != '_') && (! (isalpha(symbol[counter]))) && (! (isdigit(symbol[counter]))))
    {
      return 0;
    }
  }

  return 1;
}


int exit_function(int value)
{
//	char str_buf[200];
//	printf("Press <ENTER> to exit.\n");
//	gets(str_buf);

  exit(value);
  return value;
}

//
//
//
//
//
//

bool find_leaf(logical_expression * tree, logical_expression * leaf)
{
  for (int i = 0; i < tree->subexpressions.size(); i++)
  {
    if(strcmp(tree->subexpressions[i]->symbol, leaf->symbol) == 0)
    {
      return 1;
    }
  }
  return 0;
}

bool add_leaf(logical_expression * tree, logical_expression * leaf, char * mode)
{

  if(strcmp(mode, "true"))
  {
    if(!find_leaf(tree->subexpressions[1], leaf))
    {
      tree->subexpressions[0]->subexpressions.push_back(leaf);
      return 1;
    }
    else
    {
      return 0;
    }
  }
  else if(strcmp(mode, "false"))
  {
    if(!find_leaf(tree->subexpressions[0], leaf))
    {
      tree->subexpressions[1]->subexpressions.push_back(leaf);
      return 1;
    }
    else
    {
      return 0;
    }
  }

}


bool build_knowledge_tree(logical_expression * tree, logical_expression * knowledge_base)
{
  bool flag = 1;
  for(int i = 0; i < knowledge_base->subexpressions.size(); i++)
  {
    if(strcasecmp(knowledge_base->subexpressions[i]->connective, "") == 0)
    {
      flag = add_leaf(tree, knowledge_base->subexpressions[i], "true");
    }

    if(strcasecmp(knowledge_base->subexpressions[i]->connective, "not") == 0)
    {
      flag = add_leaf(tree, knowledge_base->subexpressions[i]->subexpressions[0], "false");
    }

    if(strcasecmp(knowledge_base->subexpressions[i]->connective, "and") == 0)
    {
      flag = build_knowledge_tree(tree, knowledge_base->subexpressions[i]);
    }

  }
  return flag;
}

int load_statement(logical_expression * tree, logical_expression * knowledge_base, logical_expression * statement)
{
  int flag = 1;
  if(strcmp(statement->connective, "") == 0)
  {
    for(int i = 0; i < knowledge_base->subexpressions.size(); i++)
    {
      if(strcasecmp(knowledge_base->connective, "iff") == 0 || strcasecmp(knowledge_base->connective, "if") == 0)
      {
        if(strcmp(knowledge_base->subexpressions[0]->symbol, statement->symbol) == 0)
        {
          if (add_leaf(tree, statement, "true"))
          {
            flag = load_statement(tree, knowledge_base, knowledge_base->subexpressions[1]);
          }
          else
          {
            return 0;
          }
        }
      }
    }
    if(flag == 1)
    {
      if(find_leaf(tree->subexpressions[0], statement) == 0)
      {
        return 1;
      }
      else if(find_leaf(tree->subexpressions[1], statement) == 0)
      {
        return 0;
      }
      else
      {
        return 2;
      }
    }
    else if (flag == 0)
    {
      return 0;
    }
    else
    {
      return 2;
    }

  }

  if(strcasecmp(statement->connective, "not") == 0)
  {
    for(int i = 0; i < knowledge_base->subexpressions.size(); i++)
    {
      if(strcasecmp(knowledge_base->connective, "iff") == 0 || strcasecmp(knowledge_base->connective, "if") == 0)
      {
        if(strcasecmp(knowledge_base->subexpressions[0]->connective, "not") == 0 &&
        strcmp(knowledge_base->subexpressions[0]->subexpressions[0]->symbol, statement->subexpressions[0]->symbol) == 0)
        if (add_leaf(tree, statement, "false"))
        {
          flag = load_statement(tree, knowledge_base, knowledge_base->subexpressions[1]);
        }
        else
        {
          return 0;
        }
      }
    }

    if(flag == 1)
    {
      if(find_leaf(tree->subexpressions[0], statement) == 0)
      {
        return 1;
      }
      else if(find_leaf(tree->subexpressions[1], statement) == 0)
      {
        return 0;
      }
      else
      {
        return 2;
      }
    }
    else if (flag == 0)
    {
      return 0;
    }
    else
    {
      return 2;
    }
  }

  if(strcasecmp(statement->connective, "or") == 0)
  {
    int k = 0;
    for(int i = 0; i < statement->subexpressions.size() && !k; i++)
    {
      k += load_statement(tree, knowledge_base, statement->subexpressions[i]);
    }
    if(k == 0)
    {
      flag = 0;
    }
  }

  if(strcasecmp(statement->connective, "xor") == 0)
  {
    int k = 0;
    for(int i = 0; i < statement->subexpressions.size() && !k; i++)
    {
      k += load_statement(tree, knowledge_base, statement->subexpressions[i]);
    }
    if(k != 1)
    {
      flag = 0;
    }
  }

  if(strcasecmp(statement->connective, "and") == 0)
  {
    int k = 0;
    for(int i = 0; i < statement->subexpressions.size(); i++)
    {
      k += load_statement(tree, knowledge_base, statement->subexpressions[i]);
    }
    if(k == statement->subexpressions.size())
    {
      flag = 1;
    }
    else if(k == 0)
    {
      flag = 2;
    }
    else
    {
      flag = 0;
    }
  }
  return flag;
}


void check_true_false(logical_expression * knowledge_base, logical_expression * statement)
{
  FILE * fp = fopen("result.txt", "wb");
  if (fp == 0)
  {
      printf("something is wrong, cannot open result.txt for writing\n");
  }

  logical_expression * tree = new logical_expression;
  logical_expression * valid = new logical_expression;
  logical_expression * notvalid = new logical_expression;

  tree->subexpressions.push_back(valid);
  tree->subexpressions.push_back(notvalid);


  if(!build_knowledge_tree(tree, knowledge_base))
  {
    fprintf(fp, "both true and false\n");
    return;
  }

  int flag = load_statement(tree, knowledge_base, statement);
  if(flag == 2)
  {
    fprintf(fp, "possibly true, possibly false");
    printf("possibly true, possibly false");
  }
  else if(flag == 1)
  {
    fprintf(fp, "definitely true");
    printf("definitely true");
  }
  else
  {
    fprintf(fp, "definitely false");
    printf("definitely false");
  }

  printf("\nCapacity %d\n", tree->subexpressions[0]->subexpressions.size());

  for(int i = 0; i < tree->subexpressions[0]->subexpressions.size(); i++)
  {
    printf("%s\n", tree->subexpressions[0]->subexpressions[i]->symbol);
  }

  printf("\nCapacity %d\n", tree->subexpressions[1]->subexpressions.size());

  for(int i = 0; i < tree->subexpressions[1]->subexpressions.size(); i++)
  {
    printf("%s\n", tree->subexpressions[1]->subexpressions[i]->symbol);
  }


  fclose(fp);
}

//
//
//
//
//
//

int main(int argc, char ** argv)
{
  char ** command_line = argv;
  if(argc != 4)
  {
  // take two arguments
    printf("Usage: %s [wumpus-rules-file] [additional-knowledge-file] [input_file]\n", command_line [0]);
    return exit_function(0);
  }

  char buffer[200];
  char * input;
  FILE * input_file;

  // read wumpus rules
  input = command_line[1];
  input_file = fopen(input, "rb");
  if (input_file == 0)
  {
    printf("failed to open file %s\n", input);
    return exit_function(0);
  }

  printf("Loading wumpus rules...\n");
  logical_expression * knowledge_base = new logical_expression();
  strcpy(knowledge_base->connective, "and");
  while(fgets(buffer, 200, input_file) != NULL)
  {
    // skip lines starting with # (comment lines), or empty lines
    if ((buffer[0] == '#') || (buffer[0] == 0) || (buffer[0] == 13) || (buffer[0] == 10))
    {
      continue;
    }
    logical_expression * subexpression = read_expression(buffer);
    knowledge_base->subexpressions.push_back(subexpression);
  }
  fclose(input_file);

  // read additional knowledge
  input = command_line[2];
  input_file = fopen(input, "rb");
  if (input_file == 0)
  {
    printf("failed to open file %s\n", input);
    return exit_function(0);
  }

  printf("Loading additional knowledge...\n");
  while(fgets(buffer, 200, input_file) != NULL)
  {
    // skip lines starting with # (comment lines), or empty lines
    if ((buffer[0] == '#') || (buffer[0] == 0) || (buffer[0] == 13) || (buffer[0] == 10))
    {
      continue;
    }
    logical_expression * subexpression = read_expression(buffer);
    knowledge_base->subexpressions.push_back(subexpression);
  }
  fclose(input_file);

  if (valid_expression(knowledge_base) == 0)
  {
    printf("invalid knowledge base\n");
    return exit_function(0);
  }

  print_expression(knowledge_base, "\n");

  // read statement whose entailment we want to determine.
  input = command_line[3];
  input_file = fopen(input, "rb");
  if (input_file == 0)
  {
    printf("failed to open file %s\n", input);
    return exit_function(0);
  }

  printf("\n\nLoading statement...\n");
  fgets(buffer, 200, input_file);
  fclose(input_file);

  logical_expression * statement = read_expression(buffer);
  if (valid_expression(statement) == 0)
  {
    printf("invalid statement\n");
    return exit_function(0);
  }

  print_expression(statement, "");

  check_true_false(knowledge_base, statement);

  delete knowledge_base;
  delete statement;
  exit_function(1);
}
