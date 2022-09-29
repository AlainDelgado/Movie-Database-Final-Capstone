package com.techelevator.controller;


import com.techelevator.dao.JdbcMovieDao;
import com.techelevator.dao.MovieDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Movie;
import com.techelevator.model.User;
import com.techelevator.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin
public class MovieController {


    MovieService movieService;
    @Autowired
    private MovieDao movieDao;
    private UserDao userDao;
    @Autowired
    private JdbcMovieDao jdbcMovieDao;


    public MovieController(MovieDao movieDao, UserDao userDao) {
        this.movieDao = movieDao;
        this.userDao = userDao;
        movieService = new MovieService();
    }

    @RequestMapping(path ="/filter/{filters}", method = RequestMethod.GET)
    @ResponseBody
    public List<Movie> getMoviesWithFilters(@PathVariable String filters) {
        System.out.println(filters);
        List<Movie> movies = new ArrayList<>();
        movies = movieService.getFilteredMovies(filters);
        return movies;
    }

    @RequestMapping(path="/now-playing", method = RequestMethod.GET)
    public List<Movie> getCurrentMovies() {
        List<Movie> movies = movieService.getNowPlaying();
        return movies;
    }

    @RequestMapping(path = "/movies", method = RequestMethod.GET)
    public List<Movie> getAllMovies() {
       System.out.println("in controller");
       List<Movie> movies = new ArrayList<>();
       movies = movieService.getAllMovies();
       return movies;

    }

    @RequestMapping(path = "/movies/{id}", method = RequestMethod.GET)
    public Movie getMovie(@Valid @RequestParam int movieId) {
        Movie movie = movieService.getMovie(movieId);
        return movie;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/movies/favorited/{id}", method = RequestMethod.PUT)
    public void favoriteMovie(Principal principal, @Valid @PathVariable int id) {
        System.out.println("test");
        jdbcMovieDao.favoriteMovie(id, 4);
    }

    @RequestMapping(path = "/movies/unfavorited/{id}", method = RequestMethod.DELETE)
    public void unfavoriteMovie(@Valid @PathVariable int id) {

    }

    @RequestMapping(path = "/movies/{userId}/favorites", method = RequestMethod.GET)
    public List<Movie> getFavoriteMovies(@Valid @PathVariable int userId, Principal principal) {
        List<Movie> favoriteMovies = new ArrayList<>();
        System.out.println("before if");
        if(userId == userDao.findIdByUsername(principal.getName())) {
            favoriteMovies = jdbcMovieDao.getFavoritedMovies(userId);
            System.out.println("made it to if");
        }
        System.out.println("after if");
        return favoriteMovies;
    }

//    @RequestMapping(path = "/movies/saved/{id}", method = RequestMethod.PUT)
//    public void saveMovie(@Valid @PathVariable int id) {
//
//    }
//
//    @RequestMapping(path = "/movies/saved/{id}", method = RequestMethod.DELETE)
//    public void unSaveMovie(@Valid @PathVariable int id) {
//
//    }
//
//    @RequestMapping(path = "/movies/{userId}/favorites", method = RequestMethod.GET)
//    public List<Movie> getSavedMovies(@Valid @PathVariable int userId, Principal principal) {
//        List<Movie> savedMovies = new ArrayList<>();
//        if(userId == userDao.findIdByUsername(principal.getName())) {
//
//        }
//        return savedMovies;
//    }



}
