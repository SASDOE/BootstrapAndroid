#!/bin/python
import json

news = [
    { 
        'id': 1,
        'title': 'Hodor and Bran Stark Won\'t Appear In \'Game of Thrones\' Season 5',
        'thumbnail_url': 'http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3862045_free.jpg',
        'content': 'Prepare for the biggest head-exploding-in-shock moment "Game of Thrones" has ever served up.',
        'date': '2 days ago',
    },
    { 
        'id': 1,
        'title': 'Hodor and Bran Stark Won\'t Appear In \'Game of Thrones\' Season 5',
        'thumbnail_url': 'http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3862045_free.jpg',
        'content': 'Prepare for the biggest head-exploding-in-shock moment "Game of Thrones" has ever served up.',
        'date': '2 days ago',
    },
]

print json.dumps(news)
