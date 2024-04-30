

from PyQt5.QtWidgets import *
from PyQt5 import QtCore, QtGui
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import random
import sys
from bin_search import BinSearch
from matrix import Matrix

def main():
    """
    tutaj znajduje się początek programu,w którym wybieramy grę oraz jej trudność
    """
    print("Welcome to Project, choose any of two games:")
    print("Write b for binsearch")
    print("Write d for determitant")
    game_choice = input("Please give your choice ")
    difficulty = int(input("Please choose a difficulty from 1,2,3: "))

    App = QApplication(sys.argv)
    if game_choice == 'b': 
        window = BinSearch(difficulty)
        sys.exit(App.exec())
    
    elif game_choice == 'd':
        window = Matrix(difficulty)
        sys.exit(App.exec())

main()