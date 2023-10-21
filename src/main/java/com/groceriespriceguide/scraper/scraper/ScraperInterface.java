package com.groceriespriceguide.scraper.scraper;

import com.groceriespriceguide.entity.Product;
import com.microsoft.playwright.Page;

import java.util.List;

public interface ScraperInterface {
    List<Product> parse(Page page);
}
