# TODO #1:30min This is a multiline todo
#  for python.
# This is a normal comment.
def main():
  """
    @todo #2:30min This is another multiline todo
     for python.
    This is a normal comment.
  """
  parser = argparse.ArgumentParser()
  parser.add_argument(
      "word",
      help="the word to be searched for in the text file."
  )
  # FIXME #3:60min This a single line todo for python.
  parser.add_argument(
      "filename",
      help="the path to the text file to be searched through"
  )