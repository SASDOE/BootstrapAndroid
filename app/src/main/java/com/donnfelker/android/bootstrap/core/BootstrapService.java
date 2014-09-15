
package com.donnfelker.android.bootstrap.core;

import com.donnfelker.android.bootstrap.R;

import java.util.Arrays;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private NewsService getNewsService() {
        return getRestAdapter().create(NewsService.class);
    }

    private CheckInService getCheckInService() {
        return getRestAdapter().create(CheckInService.class);
    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */
    public List<News> getNews() {
        List<News> news = Arrays.asList(
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", null, "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass"),
                new News("Content 1", "http://i.huffpost.com/gadgets/slideshows/208655/slide_208655_3828285_free.jpg", "Hodor and Bran Stark Won't Appear In 'Game of Thrones' Season 5"),
                new News("Content 2", "http://rack.3.mshcdn.com/media/ZgkyMDE0LzA5LzA0L2Q4L0JhaWR1RXllMS4wOTNlMy5qcGcKcAl0aHVtYgk5NTB4NTM0IwplCWpwZw/a357a1bc/cee/BaiduEye1.jpg", "Take a Look at Baidu Eye, China's Version of Google Glass")
        );
//        return getNewsService().getNews().getResults();
        return news;
    }

    public List<SPSEvent> getEvents() {
        List<SPSEvent> events = Arrays.asList(new SPSEvent("UFC Fight Night", R.drawable.ex1),
                new SPSEvent("UFC Fight Night", R.drawable.ex2),
                new SPSEvent("UFC Fight Night", R.drawable.ex3),
                new SPSEvent("UFC Fight Night", R.drawable.ex1),
                new SPSEvent("UFC Fight Night", R.drawable.ex2),
                new SPSEvent("UFC Fight Night", R.drawable.ex3),
                new SPSEvent("UFC Fight Night", R.drawable.ex1),
                new SPSEvent("UFC Fight Night", R.drawable.ex2),
                new SPSEvent("UFC Fight Night", R.drawable.ex3));
        return events;
    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
    public List<User> getUsers() {
        return getUserService().getUsers().getResults();
    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     */
    public List<CheckIn> getCheckIns() {
       return getCheckInService().getCheckIns().getResults();
    }

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }
}