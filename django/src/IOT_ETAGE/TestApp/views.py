from django.http import request
from django.shortcuts import render

# Create your views here.
def index(request):
    print("Test 1")
    print("Test 2")
    return render(request, "html/htmlOne.html", {"title": "Test", "test": "YEAH!!!"})
